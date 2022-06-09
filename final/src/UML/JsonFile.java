package UML;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import UML.Objects.ShapeCore;
import UML.Objects.ObjectFactorys.ObjectFactory_Load;
import UML.Objects.ObjectFactorys.ObjectFactoryInterface;
import UML.Objects.ObjectFactorys.ObjectFactoryInterface.ObjectType;

public class JsonFile {
    private String PATH = "../assets/json/";
    private String FileName = "uml_diagram.json";

    private ArrayList<ShapeCore> loadShapeCores;
    private ObjectFactoryInterface factory;

    public JsonFile() {
        (new File(PATH)).mkdirs();

        loadShapeCores = new ArrayList<>();
        factory = new ObjectFactory_Load();
    }

    /**
     * "@SuppressWarnings('unchecked')" BAD usage, Java generics detect this may not
     * be legal at execution time.
     * 
     * @param shapeCores
     */
    @SuppressWarnings("unchecked")
    public JSONObject shapeCoresToJSON(ArrayList<ShapeCore> shapeCores) {
        if (shapeCores == null) {
            return null;
        }

        Map<Object, Object> map = new HashMap<Object, Object>();
        JSONArray jsonArray = new JSONArray();
        for (ShapeCore shapeCore : shapeCores) {
            jsonArray.add(shapeCore.convertToJSON());
        }
        map.put("uml", jsonArray);
        return new JSONObject(map);
    }

    public void saveWorkHouse(ArrayList<ShapeCore> shapeCores) {
        this.writeJSON(this.shapeCoresToJSON(shapeCores));
    }

    /* this method can only be used in open a JSON */
    public ShapeCore idxToShapeCore(int i) {
        if (i < 0 || this.loadShapeCores == null || this.loadShapeCores.size() <= i) {
            return null;
        }
        return this.loadShapeCores.get(i);
    }

    public ArrayList<ShapeCore> openWorkHouse(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        this.loadShapeCores.clear();
        JSONArray jsonArray = (JSONArray) jsonObject.get("uml");
        for (Object tmpJsonObject : jsonArray) {
            String type = (String) ((JSONObject) tmpJsonObject).get("type");

            ShapeCore tmpShapeCore = null;
            switch (type) {
                case "Object_Class":
                    tmpShapeCore = factory.createObjectCore(ObjectType.Object_Class, null);
                    break;
                case "Object_Usecase":
                    tmpShapeCore = factory.createObjectCore(ObjectType.Object_Usecase, null);
                    break;
                case "GroupBase":
                    tmpShapeCore = factory.createObjectCore(ObjectType.Object_Group, null);
                    break;
                case "association":
                    tmpShapeCore = factory.createLineBase(ObjectType.Line_Association, null);
                    break;
                case "generalization":
                    tmpShapeCore = factory.createLineBase(ObjectType.Line_Generalization, null);
                    break;
                case "composition":
                    tmpShapeCore = factory.createLineBase(ObjectType.Line_Composition, null);
                    break;
                default:
                    break;
            }

            if (tmpShapeCore != null && tmpShapeCore.convertFromJSON(this, (JSONObject) tmpJsonObject)) {
                this.loadShapeCores.add(tmpShapeCore);
            }
        }

        return this.loadShapeCores;
    }

    private void writeJSON(JSONObject jsonObject) {
        try {
            FileWriter fileReader = new FileWriter(PATH + FileName);
            fileReader.write(jsonObject.toJSONString());
            fileReader.close();
        } catch (Exception e) {
            System.out.println("ERROR: write json fail, " + e);
            return;
        }

        System.out.println("\n");
        System.out.println("Save the UML diagram to JSON:\n" + jsonObject.toJSONString());
        return;
    }

    public JSONObject readJSON() {
        JSONObject jsonObject = null;
        try {
            FileReader fileReader = new FileReader(PATH + FileName);
            JSONParser jsonParser = new JSONParser();
            jsonObject = (JSONObject) jsonParser.parse(fileReader);
            fileReader.close();
        } catch (Exception e) {
            System.out.println("ERROR: read json fail, " + e);
            return null;
        }

        System.out.println("\n");
        System.out.println("Open the UML diagram to JSON:\n" + jsonObject.toJSONString());
        return jsonObject;
    }
}
