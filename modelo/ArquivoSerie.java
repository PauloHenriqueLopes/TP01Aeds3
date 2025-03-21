package modelo;

import entidades.Serie;
import java.io.RandomAccessFile;
import java.io.IOException;

public class ArquivoSerie {
    private RandomAccessFile arquivo;

    public ArquivoSerie() throws IOException {
        arquivo = new RandomAccessFile("./dados/serie.db", "rw");

        if (arquivo.length() == 0) {
            arquivo.writeInt(0);;
        }
    }

    public int create(Serie serie) throws Exception {
        arquivo.seek(0);
        int proxId = arquivo.readInt();
        serie.setId(++proxId);
        arquivo.seek(0);
        arquivo.write(proxId);

        arquivo.seek(arquivo.length());
        byte[] data = serie.toByteArray();

        arquivo.writeByte(0);
        arquivo.writeShort(data.length);
        arquivo.write(data);
            
        return serie.getId();
    }

    public Serie read(int id) throws Exception {
        arquivo.seek(4);

        while (arquivo.getFilePointer() < arquivo.length()) {
            long pos = arquivo.getFilePointer();
            byte lapide = arquivo.readByte();
            short tamanho = arquivo.readShort();
            byte[] data = new byte[tamanho];
            arquivo.readFully(data);
            
            if(lapide == 0) {
                Serie serie = new Serie();
                serie.fromByteArray(data);
                if(serie.getId() == id) {
                    return serie;
                }
            }
        }

        return null;
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
                if(serie.getId() == novaSerie.getId()) {
                    byte[] novoDado = novaSerie.toByteArray();

                    if(novoDado.length <= tamanho) {
                        arquivo.seek(pos + 3);
                        arquivo.write(novoDado);
                    } else {
                        arquivo.seek(pos);
                        arquivo.writeByte(1);
                        arquivo.seek(arquivo.length());
                        arquivo.writeByte(0);
                        arquivo.writeShort(novoDado.length);
                        arquivo.write(novoDado);
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

            if(lapide == 0) {
                Serie serie = new Serie();
                serie.fromByteArray(data);
                if(serie.getId() == id) {
                    arquivo.seek(pos);
                    arquivo.writeByte(1);
                    return true;
                }
            }
        }
        return false;
    }
}
