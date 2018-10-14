package ar.com.tamborindeguy.model.loaders;

import ar.com.tamborindeguy.model.readers.Loader;
import ar.com.tamborindeguy.objects.factory.ObjectFactory;
import ar.com.tamborindeguy.objects.types.Obj;
import org.ini4j.Config;
import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ObjectLoader extends Loader<Map<Integer, Obj>> {

    private static final String OBJ = "obj";
    private static final String GRH_INDEX = "GrhIndex";
    private static final String NAME = "Name";
    private static final String OBJTYPE = "ObjType";

    @Override
    public Map<Integer, Obj> load(DataInputStream file) throws IOException {
        Map<Integer, Obj> objects = new HashMap<>();
        Ini iniFile = new Ini();
        Config c = new Config();
        c.setLowerCaseSection(true);
        iniFile.setConfig(c);

        iniFile.load(file);
        int numObjs = Integer.parseInt(iniFile.get("init", "NumOBJs"));

        for (int i = 1; i <= numObjs; i++) {
            Profile.Section section = iniFile.get(OBJ + String.valueOf(i));
            if (section == null) {
                continue;
            }
            Integer type = section.get(OBJTYPE, int.class);
            Integer grhIndex = section.get(GRH_INDEX, int.class);
            String name = section.get(NAME);
            Obj object = ObjectFactory.createObject(i, type, name, grhIndex);
            ObjectFactory.fillObject(object, section);
            objects.put(i, object);
        }
        return objects;
    }
}
