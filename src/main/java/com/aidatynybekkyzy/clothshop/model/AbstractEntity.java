package com.aidatynybekkyzy.clothshop.model;

import javax.persistence.*;
import java.io.Serializable;


@MappedSuperclass
public class AbstractEntity<S extends Serializable> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected S id;

    public S getId() {
        return id;
    }

    public void setId(S id){
        this.id = id;
    }

    @Override
    public int hashCode(){
        final int prime = 17;
        int result = 1;
        result = prime * result + ((id == null) ? 0: id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity<S> that = (AbstractEntity<S>) o;
        return id.equals(that.id);
    }

    @Override
    public String toString() {
        return this.getClass() + "[id = " + id + " ]";
    }
}
