/*                                                                 
 * NOTE: This copyright does *not* cover user programs that use HQ 
 * program services by normal system calls through the application 
 * program interfaces provided as part of the Hyperic Plug-in Development 
 * Kit or the Hyperic Client Development Kit - this is merely considered 
 * normal use of the program, and does *not* fall under the heading of 
 * "derived work". 
 *  
 * Copyright (C) [2004, 2005, 2006], Hyperic, Inc. 
 * This file is part of HQ.         
 *  
 * HQ is free software; you can redistribute it and/or modify 
 * it under the terms version 2 of the GNU General Public License as 
 * published by the Free Software Foundation. This program is distributed 
 * in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the GNU General Public License for more 
 * details. 
 *                
 * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 
 * USA. 
 */

package org.hyperic.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import org.hyperic.dao.DAOFactory;

/**
 * Base class for all HQ persisted objects.
 * 
 * Some of these methods are marked as protected.  This allows Hibernate to
 * pull & set values (due to its fancy runtime subclassing), but also 
 * restricts other rogue objects from doing bad things like setting the ID
 * & version #.
 */
public abstract class PersistedObject 
    implements Serializable {
    private Integer _id;

    // for hibernate optimistic locks -- don't mess with this.
    // Named ugly-style since we already use VERSION in some of our tables.
    // really need to use Long instead of primitive value
    // because the database column can allow null version values.
    // The version column IS NULLABLE for migrated schemas. e.g. HQ upgrade
    // from 2.7.5.
    private Long    _version_;

    // list of searchable fields
    private List searchable = new ArrayList(0);

    // This is an optional feature for use by the
    // server side to perform capability based ACL checks.
    // One use case is to allow DAOs to perform RBAC via
    // PermissionManager
    private Integer subjectId;

    // XXX -- This is public for now, but should be made more private later
    protected void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public long get_version_() {
        return _version_ != null ? _version_.longValue() : 0;
    }

    protected void set_version_(Long newVer) {
        _version_ = newVer;
    }

    protected void resetSearchable()
    {
        searchable.clear();
    }

    protected void removeSearchable(String name)
    {
        searchable.remove(name);
    }

    protected void addSearchable(String name)
    {
        searchable.add(name);
    }

    public List getSearchable()
    {
        return searchable;
    }

    public Integer getSubjectId()
    {
        return subjectId;
    }

    protected void setSubjectId(Integer subjectId)
    {
        // We expose this only to the appropriate modules
        // to set subjects.
        this.subjectId = subjectId;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof PersistedObject)) {
            return false;
        }
        PersistedObject o = (PersistedObject)obj;
        
        return _id == o.getId() ||
               (_id != null && o.getId() != null && _id.equals(o.getId()));
    }

    public int hashCode() {
        int result = 17;

        result = 37*result + (_id != null ? _id.hashCode() : 0);

        return result;
    }
}
