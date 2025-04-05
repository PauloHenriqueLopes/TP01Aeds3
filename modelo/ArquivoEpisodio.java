package modelo;

import java.io.File;
import java.util.ArrayList;

import aeds3.Arquivo;
import aeds3.ArvoreBMais;
import aeds3.ParIdId;
import entidades.Episodio;

public class ArquivoEpisodio extends Arquivo<Episodio> {

    Arquivo<Episodio> arqEpisodio;
    ArvoreBMais<ParNomeId> indiceNome;
    ArvoreBMais<ParIdId> indiceSerieEpisodio;

    public ArquivoEpisodio() throws Exception {
        super("episodios", Episodio.class.getConstructor());
        File diretorio = new File("./dados/episodio");
        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }
        indiceNome = new ArvoreBMais<>(ParNomeId.class.getConstructor(), 5, "./dados/episodio" + "/indiceNome.db");

        indiceSerieEpisodio = new ArvoreBMais<>(ParIdId.class.getConstructor(), 5,
                "./dados/episodio" + "/indiceSerieEpisodio.db");
    }

    @Override
    public int create(Episodio e) throws Exception {
        int id = super.create(e);
        indiceNome.create(new ParNomeId(e.getName(), id));
        indiceSerieEpisodio.create(new ParIdId(e.getSerieId(), id));

        return id;
    }

    public Episodio[] readNome(String nome) throws Exception {
        if (nome.length() == 0)
            return null;
        ArrayList<ParNomeId> pnis = indiceNome.read(new ParNomeId(nome, -1));

        if (pnis.size() > 0) {
            Episodio[] episodios = new Episodio[pnis.size()];
            int i = 0;
            for (ParNomeId pni : pnis) {
                episodios[i++] = read(pni.getId());
            }
            return episodios;
        } else
            return null;
    }

    public Episodio[] readIdSerie(int idSerie) throws Exception {
        ArrayList<ParIdId> piis = indiceSerieEpisodio.read(new ParIdId(idSerie, -1));
        if (piis.isEmpty()) {
            return null;
        }

        Episodio[] episodios = new Episodio[piis.size()];
        int i = 0;
        for (ParIdId pii : piis) {
            episodios[i++] = read(pii.getId2());
        }
        return episodios;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Episodio e = read(id);
        if (e != null) {
            if (super.delete(id)) {
                return indiceNome.delete(new ParNomeId(e.getName(), id));
            }
        }
        return false;
    }

    public boolean delete(String nome) throws Exception {
        ArrayList<ParNomeId> pnis = indiceNome.read(new ParNomeId(nome, -1));
        if (pnis != null && pnis.size() > 0) {
            return delete(pnis.get(0).getId());
        }
        return false;
    }

    @Override
    public boolean update(Episodio novoEpisodio) throws Exception {
        Episodio e = read(novoEpisodio.getId());

        if (e != null) {
            if (super.update(novoEpisodio)) {
                if (!e.getName().equals(novoEpisodio.getName())) {
                    indiceNome.delete(new ParNomeId(e.getName(), e.getId()));
                    indiceNome.create(new ParNomeId(novoEpisodio.getName(), novoEpisodio.getId()));
                }
                return true;
            }
        }
        return false;
    }
}
