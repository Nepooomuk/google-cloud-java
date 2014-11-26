package com.google.gcloud.datastore;

import static com.google.api.services.datastore.DatastoreV1.Value.LIST_VALUE_FIELD_NUMBER;

import com.google.api.services.datastore.DatastoreV1.Value;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public final class PropertyListProperty extends
    Property<List<Property<?, ?, ?>>, PropertyListProperty, PropertyListProperty.Builder> {

  private static final long serialVersionUID = -5461475706792576395L;

  static final Marshaller<List<Property<?, ?, ?>>, PropertyListProperty, Builder> MARSHALLER =
      new BaseMarshaller<List<Property<?, ?, ?>>, PropertyListProperty, Builder>() {

    @Override
    public int getProtoFieldId() {
      return LIST_VALUE_FIELD_NUMBER;
    }

    @Override
    protected Builder newBuilder(List<Property<?, ?, ?>> properties) {
      return new Builder().setValue(properties);
    }

    @Override
    protected List<Property<?, ?, ?>> getValue(Value from) {
      List<Property<?, ?, ?>> properties = new ArrayList<>(from.getListValueCount());
      for (Value valuePb : from.getListValueList()) {
        properties.add(Property.fromPb(valuePb));
      }
      return properties;
    }

    @Override
    protected void setValue(PropertyListProperty from, Value.Builder to) {
      for (Property<?, ?, ?> property : from.getValue()) {
        to.addListValue(property.toPb());
      }
    }
  };

  public static final class Builder extends
      Property.BaseBuilder<List<Property<?, ?, ?>>, PropertyListProperty, Builder> {

    private ImmutableList.Builder<Property<?, ?, ?>> listBuilder = ImmutableList.builder();

    public Builder() {
      super(Type.PROPERTY_LIST);
      setIndexed(false);
    }

    public Builder addProperty(Property<?, ?, ?> property) {
      Preconditions.checkArgument(
          property.getType() != Type.PROPERTY_LIST, "Cannot contain another list");
      listBuilder.add(property);
      return this;
    }

    @Override
    public Builder setValue(List<Property<?, ?, ?>> properties) {
      listBuilder = ImmutableList.<Property<?, ?, ?>>builder();
      for (Property<?, ?, ?> property : properties) {
        addProperty(property);
      }
      return this;
    }

    @Override
    public List<Property<?, ?, ?>> getValue() {
      return listBuilder.build();
    }

    @Override
    protected Builder self() {
      return this;
    }

    @Override
    public PropertyListProperty build() {
      Preconditions.checkState(!getValue().isEmpty(), "Property list could not be empty");
      return new PropertyListProperty(this);
    }
  }

  public PropertyListProperty(List<Property<?, ?, ?>> properties) {
    this(new Builder().setValue(properties));
  }

  PropertyListProperty(Builder builder) {
    super(builder);
  }
}