package it.unibz.inf.ontop.dbschema.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import it.unibz.inf.ontop.dbschema.*;
import it.unibz.inf.ontop.model.type.DBTermType;
import it.unibz.inf.ontop.model.type.DBTypeFactory;
import it.unibz.inf.ontop.model.type.TypeFactory;
import it.unibz.inf.ontop.utils.ImmutableCollectors;


/**
 * OfflineMetadataProviderBuilder for tests only
 */

public class OfflineMetadataProviderBuilder {

    private final DBTypeFactory dbTypeFactory;
    private final QuotedIDFactory idFactory;
    private final ImmutableList.Builder<DatabaseRelationDefinition> listBuilder = ImmutableList.builder();

     public OfflineMetadataProviderBuilder(TypeFactory typeFactory) {
        this.idFactory = new SQLStandardQuotedIDFactory();
        this.dbTypeFactory = typeFactory.getDBTypeFactory();
    }

    public DatabaseRelationDefinition createDatabaseRelation(ImmutableList<RelationID> allIds, RelationDefinition.AttributeListBuilder builder) {
        DatabaseRelationDefinition relation = new DatabaseTableDefinition(allIds, builder);
        listBuilder.add(relation);
        return relation;
    }

    public DatabaseRelationDefinition createDatabaseRelation(String relation, String attribute1, DBTermType type1, boolean isNullable1) {
        return createDatabaseRelation(ImmutableList.of(idFactory.createRelationID(null, relation)),
                DatabaseTableDefinition.attributeListBuilder()
                .addAttribute(idFactory.createAttributeID(attribute1), type1, isNullable1));
    }

    public DatabaseRelationDefinition createDatabaseRelation(String relation, String attribute1, DBTermType type1, boolean isNullable1, String attribute2, DBTermType type2, boolean isNullable2) {
        return createDatabaseRelation(ImmutableList.of(idFactory.createRelationID(null, relation)),
                DatabaseTableDefinition.attributeListBuilder()
                        .addAttribute(idFactory.createAttributeID(attribute1), type1, isNullable1)
                        .addAttribute(idFactory.createAttributeID(attribute2), type2, isNullable2));
    }

    public DatabaseRelationDefinition createDatabaseRelation(String relation, String attribute1, DBTermType type1, boolean isNullable1, String attribute2, DBTermType type2, boolean isNullable2, String attribute3, DBTermType type3, boolean isNullable3) {
        return createDatabaseRelation(ImmutableList.of(idFactory.createRelationID(null, relation)),
                DatabaseTableDefinition.attributeListBuilder()
                        .addAttribute(idFactory.createAttributeID(attribute1), type1, isNullable1)
                        .addAttribute(idFactory.createAttributeID(attribute2), type2, isNullable2)
                        .addAttribute(idFactory.createAttributeID(attribute3), type3, isNullable3));
    }

    public DatabaseRelationDefinition createDatabaseRelation(String relation, String attribute1, DBTermType type1, boolean isNullable1, String attribute2, DBTermType type2, boolean isNullable2, String attribute3, DBTermType type3, boolean isNullable3, String attribute4, DBTermType type4, boolean isNullable4) {
        return createDatabaseRelation(ImmutableList.of(idFactory.createRelationID(null, relation)),
                DatabaseTableDefinition.attributeListBuilder()
                        .addAttribute(idFactory.createAttributeID(attribute1), type1, isNullable1)
                        .addAttribute(idFactory.createAttributeID(attribute2), type2, isNullable2)
                        .addAttribute(idFactory.createAttributeID(attribute3), type3, isNullable3)
                        .addAttribute(idFactory.createAttributeID(attribute4), type4, isNullable4));
    }

    public DatabaseRelationDefinition createDatabaseRelation(String relation, String attribute1, DBTermType type1, boolean isNullable1, String attribute2, DBTermType type2, boolean isNullable2, String attribute3, DBTermType type3, boolean isNullable3, String attribute4, DBTermType type4, boolean isNullable4, String attribute5, DBTermType type5, boolean isNullable5) {
        return createDatabaseRelation(ImmutableList.of(idFactory.createRelationID(null, relation)),
                DatabaseTableDefinition.attributeListBuilder()
                        .addAttribute(idFactory.createAttributeID(attribute1), type1, isNullable1)
                        .addAttribute(idFactory.createAttributeID(attribute2), type2, isNullable2)
                        .addAttribute(idFactory.createAttributeID(attribute3), type3, isNullable3)
                        .addAttribute(idFactory.createAttributeID(attribute4), type4, isNullable4)
                        .addAttribute(idFactory.createAttributeID(attribute5), type5, isNullable5));
    }

    public DatabaseRelationDefinition createDatabaseRelation(String relation, String attribute1, DBTermType type1, boolean isNullable1, String attribute2, DBTermType type2, boolean isNullable2, String attribute3, DBTermType type3, boolean isNullable3, String attribute4, DBTermType type4, boolean isNullable4, String attribute5, DBTermType type5, boolean isNullable5, String attribute6, DBTermType type6, boolean isNullable6) {
        return createDatabaseRelation(ImmutableList.of(idFactory.createRelationID(null, relation)),
                DatabaseTableDefinition.attributeListBuilder()
                        .addAttribute(idFactory.createAttributeID(attribute1), type1, isNullable1)
                        .addAttribute(idFactory.createAttributeID(attribute2), type2, isNullable2)
                        .addAttribute(idFactory.createAttributeID(attribute3), type3, isNullable3)
                        .addAttribute(idFactory.createAttributeID(attribute4), type4, isNullable4)
                        .addAttribute(idFactory.createAttributeID(attribute5), type5, isNullable5)
                        .addAttribute(idFactory.createAttributeID(attribute6), type6, isNullable6));
    }

    public MetadataProvider build() {
        ImmutableList<DatabaseRelationDefinition> list = listBuilder.build();
        return new ImmutableMetadataProvider(getDBParameters(), list.stream()
                .flatMap(r -> r.getAllIDs().stream().map(i -> Maps.immutableEntry(i, r)))
                .collect(ImmutableCollectors.toMap()));
    }

    public QuotedIDFactory getQuotedIDFactory() {
        return idFactory;
    }

    public DBTypeFactory getDBTypeFactory() {
        return dbTypeFactory;
    }

    private DBParameters getDBParameters() {
        return new DBParameters() {
            @Override
            public QuotedIDFactory getQuotedIDFactory() {
                return idFactory;
            }

            @Override
            public DBTypeFactory getDBTypeFactory() {
                return dbTypeFactory;
            }

            @Override
            public String getDriverName() {
                return null;
            }

            @Override
            public String getDriverVersion() {
                return null;
            }

            @Override
            public String getDbmsProductName() {
                return null;
            }

            @Override
            public String getDbmsVersion() {
                return null;
            }
        };
    }
}
