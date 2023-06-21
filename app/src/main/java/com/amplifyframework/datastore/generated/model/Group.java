package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Group type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Groups", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class Group implements Model {
  public static final QueryField ID = field("Group", "id");
  public static final QueryField MEMBERS = field("Group", "members");
  public static final QueryField LOCATION = field("Group", "location");
  public static final QueryField TIME = field("Group", "time");
  public static final QueryField SPECIAL_REQUEST = field("Group", "specialRequest");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) List<String> members;
  private final @ModelField(targetType="String") String location;
  private final @ModelField(targetType="String") String time;
  private final @ModelField(targetType="String") String specialRequest;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String resolveIdentifier() {
    return id;
  }
  
  public String getId() {
      return id;
  }
  
  public List<String> getMembers() {
      return members;
  }
  
  public String getLocation() {
      return location;
  }
  
  public String getTime() {
      return time;
  }
  
  public String getSpecialRequest() {
      return specialRequest;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Group(String id, List<String> members, String location, String time, String specialRequest) {
    this.id = id;
    this.members = members;
    this.location = location;
    this.time = time;
    this.specialRequest = specialRequest;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Group group = (Group) obj;
      return ObjectsCompat.equals(getId(), group.getId()) &&
              ObjectsCompat.equals(getMembers(), group.getMembers()) &&
              ObjectsCompat.equals(getLocation(), group.getLocation()) &&
              ObjectsCompat.equals(getTime(), group.getTime()) &&
              ObjectsCompat.equals(getSpecialRequest(), group.getSpecialRequest()) &&
              ObjectsCompat.equals(getCreatedAt(), group.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), group.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getMembers())
      .append(getLocation())
      .append(getTime())
      .append(getSpecialRequest())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Group {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("members=" + String.valueOf(getMembers()) + ", ")
      .append("location=" + String.valueOf(getLocation()) + ", ")
      .append("time=" + String.valueOf(getTime()) + ", ")
      .append("specialRequest=" + String.valueOf(getSpecialRequest()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static MembersStep builder() {
      return new Builder();
  }
  
  /**
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static Group justId(String id) {
    return new Group(
      id,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      members,
      location,
      time,
      specialRequest);
  }
  public interface MembersStep {
    BuildStep members(List<String> members);
  }
  

  public interface BuildStep {
    Group build();
    BuildStep id(String id);
    BuildStep location(String location);
    BuildStep time(String time);
    BuildStep specialRequest(String specialRequest);
  }
  

  public static class Builder implements MembersStep, BuildStep {
    private String id;
    private List<String> members;
    private String location;
    private String time;
    private String specialRequest;
    @Override
     public Group build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Group(
          id,
          members,
          location,
          time,
          specialRequest);
    }
    
    @Override
     public BuildStep members(List<String> members) {
        Objects.requireNonNull(members);
        this.members = members;
        return this;
    }
    
    @Override
     public BuildStep location(String location) {
        this.location = location;
        return this;
    }
    
    @Override
     public BuildStep time(String time) {
        this.time = time;
        return this;
    }
    
    @Override
     public BuildStep specialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
        return this;
    }
    
    /**
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, List<String> members, String location, String time, String specialRequest) {
      super.id(id);
      super.members(members)
        .location(location)
        .time(time)
        .specialRequest(specialRequest);
    }
    
    @Override
     public CopyOfBuilder members(List<String> members) {
      return (CopyOfBuilder) super.members(members);
    }
    
    @Override
     public CopyOfBuilder location(String location) {
      return (CopyOfBuilder) super.location(location);
    }
    
    @Override
     public CopyOfBuilder time(String time) {
      return (CopyOfBuilder) super.time(time);
    }
    
    @Override
     public CopyOfBuilder specialRequest(String specialRequest) {
      return (CopyOfBuilder) super.specialRequest(specialRequest);
    }
  }
  
}
