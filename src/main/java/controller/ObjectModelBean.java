/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.json.*;
import javax.json.stream.JsonGenerator;

/**
 *
 * @author osvaldoberg
 */
@Named(value = "objectModelBean")
@SessionScoped
public class ObjectModelBean implements Serializable {

     /* JSON model information */
    protected String documentJson;
    protected String documentJsonFormatted;
    List<DOMTreeRow> rowList;
    
    /* Form properties */
    private String name = "Controlando o Estresse";
    private String author = "Osvaldo Berg Junior";
    private String coAuthor = "Valeriana";
    private String isbn = "10-202-303-5";
    private int year = 2020;
    private String category = "Motivacao";
    private float price = (float) 20.5;
    protected String jsonTextArea = "";
    
    static final Logger log = Logger.getLogger("ObjectModelBean");
    
    public ObjectModelBean() {}
    
        
    public String getJsonTextArea() {
        return jsonTextArea;
    }
    public void setJsonTextArea(String jsonTextArea) {
        this.jsonTextArea = jsonTextArea;
    }
    public String getDocumentJson() {
        return documentJson;
    }
    public String getDocumentJsonFormatted() {
        return documentJsonFormatted;
    }
    public List<DOMTreeRow> getRowList() {
        return rowList;
    }
    
    /* Action method for the form in index.xhtml.
     * Builds a JSON object model from form data. */
    public String buildJson() {        
        /* Build JSON Object Model */
        JsonObject model = Json.createObjectBuilder()
            .add("name", getName())
            .add("author", getAuthor())
            .add("coAuthor", getCoAuthor())
            .add("isbn", getIsbn())
            .add("year", getYear())
            .add("category", getCategory())
            .add("price", getPrice())
        .build();
        
        /* Write JSON Output */
        StringWriter stWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stWriter)) {
            jsonWriter.writeObject(model);
        }
        documentJson = stWriter.toString();
        
        /* Write formatted JSON Output */
        Map<String,String> config = new HashMap<>();
        config.put(JsonGenerator.PRETTY_PRINTING, "");
        JsonWriterFactory factory = Json.createWriterFactory(config);
        
        StringWriter stWriterF = new StringWriter();
        try (JsonWriter jsonWriterF = factory.createWriter(stWriterF)) {
            jsonWriterF.writeObject(model);
        }
        documentJsonFormatted = stWriterF.toString();
        jsonTextArea = documentJsonFormatted;
        
        /* JSF navigation */
        return "modelcreated";
    }
    
    /* Action method for form in modelcreated.xhtml.
     * Parses JSON data from the textarea. */
    public String parseJson() {
        /* Parse the data using the document object model approach */
        JsonStructure parsed;
        try (JsonReader reader = Json.createReader(new StringReader(jsonTextArea))) {
            parsed = reader.readObject();
        }

        /* Represent the DOM tree on a list for a JSF table */
        rowList = new ArrayList<>();
        this.printTree(parsed, 0, "");
        
        /* JSF navigation */
        return "parsejson";
    }
    
    /* Used to populate rowList to display the DOM tree on a JSF table */
    public void printTree(JsonValue tree, int level, String key) {
        switch (tree.getValueType()) {
            case OBJECT:
                JsonObject object = (JsonObject) tree;
                rowList.add(new DOMTreeRow(level, tree.getValueType().toString(), key, "--"));
                for (String name : object.keySet()) {
                   this.printTree(object.get(name), level+1, name);
                }
                break;
            case ARRAY:
                JsonArray array = (JsonArray) tree;
                rowList.add(new DOMTreeRow(level, tree.getValueType().toString(), key, "--"));
                for (JsonValue val : array) {
                    this.printTree(val, level+1, "");
                }
                break;
            case STRING:
                JsonString st = (JsonString) tree;
                rowList.add(new DOMTreeRow(level, tree.getValueType().toString(), key, st.getString()));
                break;
            case NUMBER:
                JsonNumber num = (JsonNumber) tree;
                rowList.add(new DOMTreeRow(level, tree.getValueType().toString(), key, num.toString()));
                break;
            case FALSE:
            case TRUE:
            case NULL:
                String valtype = tree.getValueType().toString();
                rowList.add(new DOMTreeRow(level, valtype, key, valtype.toLowerCase()));
                break;
        }
    }
    
    /* Used for showing the JSON DOM tree as rows in a JSF table */
    public class DOMTreeRow {
        private int level;
        private String type;
        private String name;
        private String value;
        
        public DOMTreeRow(int level, String type, String name, String value) {
            this.level = level;
            this.type = type;
            this.name = name;
            this.value = value;
        }
        
        public int getLevel() { return level; }
        public String getType() { return type; }
        public String getName() { return name; }
        public String getValue() { return value; }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return the coAuthor
     */
    public String getCoAuthor() {
        return coAuthor;
    }

    /**
     * @param coAuthor the coAuthor to set
     */
    public void setCoAuthor(String coAuthor) {
        this.coAuthor = coAuthor;
    }

    /**
     * @return the isbn
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * @param isbn the isbn to set
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the price
     */
    public float getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(float price) {
        this.price = price;
    }
    
}
