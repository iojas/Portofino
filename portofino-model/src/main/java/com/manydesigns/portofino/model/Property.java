package com.manydesigns.portofino.model;

import org.apache.commons.configuration2.Configuration;

import javax.xml.bind.Unmarshaller;
import java.util.ArrayList;
import java.util.List;

public class Property implements ModelObject, Annotated {

    protected Entity owner;
    protected String name;
    protected final List<Annotation> annotations = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void afterUnmarshal(Unmarshaller u, Object parent) {
        this.owner = (Entity) parent;
    }

    @Override
    public void reset() {

    }

    @Override
    public void init(Model model, Configuration configuration) {

    }

    @Override
    public void link(Model model, Configuration configuration) {

    }

    @Override
    public void visitChildren(ModelObjectVisitor visitor) {
        for (Annotation annotation : annotations) {
            visitor.visit(annotation);
        }
    }

    public Entity getOwner() {
        return owner;
    }

    public void setOwner(Entity owner) {
        this.owner = owner;
    }

    @Override
    public List<Annotation> getAnnotations() {
        return annotations;
    }
}
