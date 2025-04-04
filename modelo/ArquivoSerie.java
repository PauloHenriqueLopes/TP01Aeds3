package modelo;

import entidades.Serie;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import aeds3.ArvoreBMais;

public class ArquivoSerie {
    private RandomAccessFile arquivo;
    private ArvoreBMais<ParNomeId> indiceNome;

    public ArquivoSerie() throws Exception {
        arquivo = new RandomAccessFile("./dados/serie.db", "rw");
        indiceNome = new ArvoreBMais<>(ParNomeId.class.getConstructor(), 5, "./dados/indiceNomeSerie.db");

        if (arquivo.length() == 0) {
            arquivo.writeInt(0); // Cabeçalho: Próximo ID disponível
        }
    }

    public int create(Serie serie) throws Exception {
        arquivo.seek(0);
        int id = arquivo.readInt();
        serie.setId(id);
        byte[] data = serie.toByteArray();

        arquivo.seek(arquivo.length()); // Adiciona no final do arquivo
        long pos = arquivo.getFilePointer();
        arquivo.writeByte(0); // Lápide (0 = válido, 1 = excluído)
        arquivo.writeShort(data.length);
        arquivo.write(data);

        arquivo.seek(0);
        arquivo.writeInt(id + 1); // Atualiza o próximo ID disponível

        // Adiciona no índice B+
        indiceNome.create(new ParNomeId(serie.getName(), id));

        return id;
    }

    public Serie read(int id) throws Exception {
        arquivo.seek(4);

        while (arquivo.getFilePointer() < arquivo.length()) {
            long pos = arquivo.getFilePointer();
            byte lapide = arquivo.readByte();
            short tamanho = arquivo.readShort();
            byte[] data = new byte[tamanho];
            arquivo.readFully(data);

            if (lapide == 0) {
                Serie serie = new Serie();
                serie.fromByteArray(data);
                if (serie.getId() == id) {
                    return serie;
                }
            }
        }
        return null;
    }

    public Serie[] readNome(String nome) throws Exception {
        if(nome.length() == 0) return null;
        ArrayList<ParNomeId> pnis = indiceNome.read(new ParNomeId(nome, -1));
        if(pnis.size() > 0) {
            Serie[] series = new Serie[pnis.size()];
            int i = 0;
            for(ParNomeId pni : pnis) {
                series[i++] = read(pni.getId());
            }
            return series;
        } else {
            return null;
        }
    }

    public boolean update(Serie novaSerie) throws Exception {
        arquivo.seek(4);

        while (arquivo.getFilePointer() < arquivo.length()) {
            long pos = arquivo.getFilePointer();
            byte lapide = arquivo.readByte();
            short tamanho = arquivo.readShort();
            byte[] data = new byte[tamanho];
            arquivo.readFully(data);

            if (lapide == 0) {
                Serie serie = new Serie();
                serie.fromByteArray(data);
                if (serie.getId() == novaSerie.getId()) {
                    byte[] novoDado = novaSerie.toByteArray();

                    if (novoDado.length <= tamanho) {
                        arquivo.seek(pos + 3);
                        arquivo.write(novoDado);
                    } else {
                        // Marca como excluído e realoca no final
                        arquivo.seek(pos);
                        arquivo.writeByte(1);
                        arquivo.seek(arquivo.length());
                        arquivo.writeByte(0);
                        arquivo.writeShort(novoDado.length);
                        arquivo.write(novoDado);
                    }

                    // Atualiza índice se o nome mudou
                    if (!serie.getName().equals(novaSerie.getName())) {
                        indiceNome.delete(new ParNomeId(serie.getName(), serie.getId()));
                        indiceNome.create(new ParNomeId(novaSerie.getName(), novaSerie.getId()));
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean delete(int id) throws Exception {
        arquivo.seek(4);

        while (arquivo.getFilePointer() < arquivo.length()) {
            long pos = arquivo.getFilePointer();
            byte lapide = arquivo.readByte();
            short tamanho = arquivo.readShort();
            byte[] data = new byte[tamanho];
            arquivo.readFully(data);

            if (lapide == 0) {
                Serie serie = new Serie();
                serie.fromByteArray(data);
                if (serie.getId() == id) {
                    arquivo.seek(pos);
                    arquivo.writeByte(1);

                    // Remove do índice B+
                    indiceNome.delete(new ParNomeId(serie.getName(), id));

                    return true;
                }
            }
        }
        return false;
    }
}
