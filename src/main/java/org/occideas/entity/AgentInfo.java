package org.occideas.entity;

import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@XmlRootElement
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "agent_discriminator", discriminatorType = DiscriminatorType.STRING)
public class AgentInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idAgent;
    private String name;
    private String description;
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date lastUpdated;
    private int deleted;

    public AgentInfo() {
        super();
    }

    public AgentInfo(long id) {
        this.idAgent = id;
    }

    public long getIdAgent() {
        return idAgent;
    }

    public void setIdAgent(long idAgent) {
        this.idAgent = idAgent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof AgentInfo))
            return false;
        AgentInfo castOther = (AgentInfo) other;

        return (this.getIdAgent() == castOther.getIdAgent());

    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

}
