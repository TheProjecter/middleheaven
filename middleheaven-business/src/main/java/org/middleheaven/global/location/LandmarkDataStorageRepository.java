package org.middleheaven.global.location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.middleheaven.domain.criteria.EntityCriteriaBuilder;
import org.middleheaven.domain.store.DomainStore;
import org.middleheaven.quantity.measure.AngularMeasure;

public class LandmarkDataStorageRepository extends LandmarkRepository {


	private DomainStore storage;


	private class CategorizedLandmark {
		private Landmark landmark;
		private LandmarkCategory category;

		public CategorizedLandmark() {}

		public CategorizedLandmark(Landmark landmark, String category) {
			super();
			this.landmark = landmark;
			this.category = new LandmarkCategory(category);
		}

	}
	@Override
	public void addLandmark(Landmark landmark, String category) {
		CategorizedLandmark cl = new CategorizedLandmark(landmark, category);
		storage.store(cl);
	}

	@Override
	public void removeLandmark(Landmark landmark, String category) {
		CategorizedLandmark cl = new CategorizedLandmark(landmark, category);
		storage.store(cl);
	}

	@Override
	public void deleteCategory(String category) {
		storage.remove(new LandmarkCategory(category));
	}

	@Override
	public void deleteLandmark(Landmark landmark) {
		storage.remove(landmark);
	}

	@Override
	public Iterable<String> getCategories() {
		Collection<LandmarkCategory> all = storage.createQuery(
				EntityCriteriaBuilder.search(LandmarkCategory.class).all()
		).execute().fetchAll();
		List<String> names = new ArrayList<String>(all.size());
		for (LandmarkCategory category : all){
			names.add(category.getName());
		}
		return names;
	}

	@Override
	public Iterable<Landmark> getLandmarks() {
		return storage.createQuery(EntityCriteriaBuilder.search(Landmark.class).all()).execute().fetchAll();
	}

	@Override
	public Iterable<Landmark> getLandmarks(String category, 
			AngularMeasure minLongitude,AngularMeasure minLatitude, 
			AngularMeasure maxLongitude,AngularMeasure maxLatitude) {

		return storage.createQuery(EntityCriteriaBuilder.search(Landmark.class)
				.and("coordinates.latitude").bewteen(minLatitude,maxLatitude)
				.and("coordinates.longitude").bewteen(minLongitude, maxLongitude)
				.and("category").eq(category)
				.all()
		).execute().fetchAll();
	}

	@Override
	public Iterable<Landmark> getLandmarks(String category, String name) {
		return storage.createQuery(EntityCriteriaBuilder.search(Landmark.class)
				.and("category").eq(category)
				.or("name").eq(name)
				.all()
		).execute().fetchAll();
	}




}
