//package org.middleheaven.persistance.xml;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Map;
//import java.util.TreeMap;
//
//import org.middleheaven.util.coersion.TypeCoercing;
//import org.middleheaven.util.criteria.QualifiedName;
//import org.middleheaven.util.identity.Identity;
//import org.middleheaven.util.identity.IntegerIdentity;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//
//public class NodeStorable implements EntityInstance{
//
//	Map<String, String> data = new TreeMap<String,String>();
//	Identity key;
//	private StorableEntityModel model;
//	
//	public NodeStorable(Node node , StorableEntityModel model) {
//		this.model = model;
//		
//		NodeList fields = node.getChildNodes();
//		for (int i = 0; i < fields.getLength(); i++) {
//			Node field = fields.item(i);
//			data.put(field.getNodeName(), field.getTextContent());
//		}
//		String val = data.get(model.identityFieldModel().getHardName().getName());
//
//		key = val ==null ? null : IntegerIdentity.valueOf(val);
//	}
//
//	@Override
//	public Object getFieldValue(EntityFieldModel fieldModel) {
//		if (fieldModel.isIdentity()){
//			return key;
//		}
//		
//		QualifiedName name = model.columnModel(fieldModel.getSimpleName()).getHardName();
//		
//		if (fieldModel.getDataType().isTemporal()){
//			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//			Date date;
//			try {
//				date = format.parse(data.get(name.getName()));
//			} catch (ParseException e) {
//				throw new RuntimeException(e);
//			}
//			
//			return date;
//		} else {
//			return TypeCoercing.coerce(data.get(name.getName()), fieldModel.getValueType());
//		}
//	}
//
//	@Override
//	public Identity getIdentity() {
//		return key;
//	}
//
//	@Override
//	public Class<?> getPersistableClass() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public StorableState getStorableState() {
//		return StorableState.RETRIVED;
//	}
//
//	@Override
//	public void setFieldValue(EntityFieldModel model, Object fieldValue) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void setIdentity(Identity id) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void setStorableState(StorableState state) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void addFieldElement(EntityFieldModel model, Object element) {
//		// TODO implement Storable.addFieldElement
//		
//	}
//
//	@Override
//	public void removeFieldElement(EntityFieldModel model, Object element) {
//		// TODO implement Storable.removeFieldElement
//		
//	}
//
//	@Override
//	public EntityModel getEntityModel() {
//		// TODO implement Storable.getEntityModel
//		return null;
//	}
//
//
//}
