package org.middleheaven.global.location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.middleheaven.quantity.measure.AngularMeasure;
import org.middleheaven.storage.EntityStore;
import org.middleheaven.storage.criteria.CriteriaBuilder;

public class LandmarkDataStorageRepository extends LandmarkRepository {


	private EntityStore storage;


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
				CriteriaBuilder.search(LandmarkCategory.class).all()
		).all();
		List<String> names = new ArrayList<String>(all.size());
		for (LandmarkCategory category : all){
			names.add(category.getName());
		}
		return names;
	}

	@Override
	public Iterable<Landmark> getLandmarks() {
		return storage.createQuery(CriteriaBuilder.search(Landmark.class).all()).all();
	}

	@Override
	public Iterable<Landmark> getLandmarks(String category, 
			AngularMeasure minLongitude,AngularMeasure minLatitude, 
			AngularMeasure maxLongitude,AngularMeasure maxLatitude) {

		return storage.createQuery(CriteriaBuilder.search(Landmark.class)
				.and("coordinates.latitude").bewteen(minLatitude,maxLatitude)
				.and("coordinates.longitude").bewteen(minLongitude, maxLongitude)
				.and("category").eq(category)
				.all()
		).all();
	}

	@Override
	public Iterable<Landmark> getLandmarks(String category, String name) {
		return storage.createQuery(CriteriaBuilder.search(Landmark.class)
				.and("category").eq(category)
				.or("name").eq(name)
				.all()
		).all();
	}




}
