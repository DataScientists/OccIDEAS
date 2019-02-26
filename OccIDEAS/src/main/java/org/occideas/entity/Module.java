package org.occideas.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@DiscriminatorValue("M")
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
@SelectBeforeUpdate(value = true)
public class Module extends Node<Question> implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = -7963940691772676956L;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "idModule", referencedColumnName = "idNode", updatable = false)
    protected List<ModuleRule> moduleRule;


    public Module()
    {
        super();
    }
    
    public Module(Node node)
    {
        super(node);
    }


    public Module(long idNode)
    {
        super();
        this.setIdNode(idNode);
    }


    public Module(String idNode)
    {
        super();
        this.setIdNode(Long.parseLong(idNode));
    }


    public void addNote(Note note)
    {
        note.setNode(this);
        this.setNotes(this.getNotes() == null ? new ArrayList<Note>() : this.getNotes());
        this.getNotes().add(note);
    }


    public List<ModuleRule> getModuleRule()
    {
        return moduleRule;
    }


    public void setModuleRule(List<ModuleRule> moduleRule)
    {
        this.moduleRule = moduleRule;
    }

}
