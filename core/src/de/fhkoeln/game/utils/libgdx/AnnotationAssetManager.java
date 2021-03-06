/** Copyright 2014 Robin Stumm (serverkorken@gmail.com, http://dermetfan.net)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. */

package de.fhkoeln.game.utils.libgdx;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

/** An {@link com.badlogic.gdx.assets.AssetManager} that {@link com.badlogic.gdx.assets.AssetManager#load(com.badlogic.gdx.assets.AssetDescriptor) loads} assets from a container class using reflection.
 *  @author dermetfan */
public class AnnotationAssetManager extends AssetManager {

	/** Indicates whether a field should be {@link de.fhkoeln.game.utils.libgdx.AnnotationAssetManager#load(java.lang.reflect.Field) loaded} and which {@link com.badlogic.gdx.assets.AssetDescriptor#type} to use if necessary.
	 *  @author dermetfan */
	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Asset {

		/** @return whether this field should be loaded */
		boolean load() default true;

		/** @return the {@link com.badlogic.gdx.assets.AssetDescriptor#type} to use */
		Class<?> type() default void.class;

	}

	/** @see de.fhkoeln.game.utils.libgdx.AnnotationAssetManager#AnnotationAssetManager(com.badlogic.gdx.assets.loaders.FileHandleResolver) */
	public AnnotationAssetManager() {
		this(new InternalFileHandleResolver());
	}

	/** @see com.badlogic.gdx.assets.AssetManager#AssetManager(com.badlogic.gdx.assets.loaders.FileHandleResolver)
	 *  @see com.badlogic.gdx.assets.AssetManager#setLoader(Class, com.badlogic.gdx.assets.loaders.AssetLoader) */
	public AnnotationAssetManager(FileHandleResolver resolver) {
		super(resolver);
	}

	/** {@link #load(java.lang.reflect.Field) Loads} all fields in the given {@code container} class if they are annotated with {@link de.fhkoeln.game.utils.libgdx.AnnotationAssetManager.Asset} and {@link de.fhkoeln.game.utils.libgdx.AnnotationAssetManager.Asset#load()} is true.
	 *  @param container the class containing the fields whose {@link com.badlogic.gdx.assets.AssetDescriptor AssetDescriptors} to load
	 *  @param instance the instance of the class containing the given {@code field} (may be null if all fields in the class annotated with {@link de.fhkoeln.game.utils.libgdx.AnnotationAssetManager.Asset} are static) */
	public <T> void load(Class<? extends T> container, T instance) {
		for(Field field : container.getFields())
			if(field.isAnnotationPresent(Asset.class) && field.getAnnotation(Asset.class).load())
				load(field, instance);
	}

	/** @param instance the instance of a container class from which to load fields annotated with {@link de.fhkoeln.game.utils.libgdx.AnnotationAssetManager.Asset}
	 *  @see #load(Class, Object) */
	public void load(Object instance) {
		load(instance.getClass(), instance);
	}

	/** @param container the class with the fields annotated with {@link de.fhkoeln.game.utils.libgdx.AnnotationAssetManager.Asset} (must all be static, use {@link #load(Class, Object)} otherwise)
	 *  @see #load(Class, Object) */
	public void load(Class<?> container) {
		load(container, null);
	}

	/** {@link com.badlogic.gdx.assets.AssetManager#load(String, Class) loads} the given field
	 *  @param field the field to load
	 *  @param instance the instance of the class containing the given field (may be null if it's static) */
	@SuppressWarnings("unchecked")
	public void load(Field field, Object instance) {
		String path = getAssetPath(field, instance);
		Class<?> type = getAssetType(field, instance);
		@SuppressWarnings("rawtypes")
		AssetLoaderParameters params = getAssetLoaderParameters(field, instance);
		if(path != null && type != null)
			if(params == null)
				load(path, type);
			else
				load(path, type, params);
	}

	/** @param field the static field to load
	 *  @see #load(java.lang.reflect.Field, Object) */
	public void load(Field field) {
		load(field, null);
	}

	/** @param field the field to get the asset path from
	 *  @param instance an instance of the class containing the given field
	 *  @return the asset path stored by the field */
	public static String getAssetPath(Field field, Object instance) {
		String path = null;
		try {
			Object content = field.get(instance);
			if(content instanceof AssetDescriptor)
				path = ((AssetDescriptor<?>) content).fileName;
			else if(content instanceof String)
				path = (String) content;
			else if(content instanceof FileHandle)
				path = ((FileHandle) content).path();
		} catch(IllegalArgumentException | IllegalAccessException e) {
			Gdx.app.error(AnnotationAssetManager.class.getSimpleName(), "could not access field \"" + field.getName() + "\"", e);
		}
		return path;
	}

	/** @return the {@link de.fhkoeln.game.utils.libgdx.AnnotationAssetManager.Asset#type()} of the given Field */
	public static Class<?> getAssetType(Field field, Object instance) {
		if(AssetDescriptor.class.isAssignableFrom(field.getType()))
			try {
				return ((AssetDescriptor<?>) field.get(instance)).type;
			} catch(IllegalArgumentException | IllegalAccessException e) {
				Gdx.app.error(AnnotationAssetManager.class.getSimpleName(), "could not access field \"" + field.getName() + "\"", e);
			}
		if(field.isAnnotationPresent(Asset.class))
			return field.getAnnotation(Asset.class).type();
		return null;
	}

	/** @return the {@link com.badlogic.gdx.assets.AssetDescriptor#params AssetLoaderParameters} of the AssetDescriptor in the given field */
	@SuppressWarnings("unchecked")
	public static <T> AssetLoaderParameters<T> getAssetLoaderParameters(Field field, Object instance) {
		if(AssetDescriptor.class.isAssignableFrom(field.getType()))
			try {
				return ((AssetDescriptor<T>) field.get(instance)).params;
			} catch(IllegalArgumentException | IllegalAccessException e) {
				Gdx.app.error(AnnotationAssetManager.class.getSimpleName(), "could not access field\"" + field.getName() + "\"", e);
			}
		return null;
	}

	/** Creates an {@link com.badlogic.gdx.assets.AssetDescriptor} from a field that is annotated with {@link de.fhkoeln.game.utils.libgdx.AnnotationAssetManager.Asset}. The field's type must be {@code String} or {@link com.badlogic.gdx.files.FileHandle} and the {@link de.fhkoeln.game.utils.libgdx.AnnotationAssetManager.Asset#type()} must not be primitive.
	 *  @param field the field annotated with {@link de.fhkoeln.game.utils.libgdx.AnnotationAssetManager.Asset} to create an {@link com.badlogic.gdx.assets.AssetDescriptor} from
	 *  @param instance the instance of the class containing the given {@code field}
	 *  @return an {@link com.badlogic.gdx.assets.AssetDescriptor} created from the given, with {@link de.fhkoeln.game.utils.libgdx.AnnotationAssetManager.Asset} annotated field (may be null if all fields in the class annotated with {@link de.fhkoeln.game.utils.libgdx.AnnotationAssetManager.Asset} are static) */
	@SuppressWarnings("unchecked")
	public <T> AssetDescriptor<T> createAssetDescriptor(Field field, Object instance) {
		if(!field.isAnnotationPresent(Asset.class))
			return null;
		Class<?> fieldType = field.getType();
		if(fieldType != String.class && fieldType != FileHandle.class && fieldType != AssetDescriptor.class) {
			Gdx.app.error(AnnotationAssetManager.class.getSimpleName(), "type of @" + Asset.class.getSimpleName() + " field \"" + field.getName() + "\" must be " + String.class.getSimpleName() + " or " + FileHandle.class.getSimpleName() + " to create an " + AssetDescriptor.class.getSimpleName() + " from it");
			return null;
		}
		Class<?> type = getAssetType(field, instance);
		if(type.isPrimitive()) {
			Gdx.app.error(AnnotationAssetManager.class.getSimpleName(), "cannot create an " + AssetDescriptor.class.getSimpleName() + " of the generic type " + type.getSimpleName() + " from the @" + Asset.class.getSimpleName() + " field \"" + field.getName() + "\"");
			return null;
		}
		if(fieldType == AssetDescriptor.class)
			try {
				AssetDescriptor<?> alreadyExistingDescriptor = (AssetDescriptor<?>) field.get(instance);
				if(alreadyExistingDescriptor.type == type)
					return (AssetDescriptor<T>) alreadyExistingDescriptor;
				else
					return new AssetDescriptor<>(alreadyExistingDescriptor.file, (Class<T>) type);
			} catch(IllegalArgumentException | IllegalAccessException e) {
				Gdx.app.error(AnnotationAssetManager.class.getSimpleName(), "couldn't access field \"" + field.getName() + "\"", e);
			}
		else
			try {
				if(fieldType == String.class)
					return new AssetDescriptor<>((String) field.get(instance), (Class<T>) type);
				else
					return new AssetDescriptor<>((FileHandle) field.get(instance), (Class<T>) type);
			} catch(IllegalArgumentException | IllegalAccessException e) {
				Gdx.app.error(AnnotationAssetManager.class.getSimpleName(), "couldn't access field \"" + field.getName() + "\"", e);
			}
		return null;
	}

	/** creates an {@link com.badlogic.gdx.assets.AssetDescriptor} from a static field
	 *  @param field the field annotated with {@link de.fhkoeln.game.utils.libgdx.AnnotationAssetManager.Asset} to create an {@link com.badlogic.gdx.assets.AssetDescriptor} from (must be static)
	 *  @return the {@link com.badlogic.gdx.assets.AssetDescriptor} created from the given static {@code field} annotated with {@link de.fhkoeln.game.utils.libgdx.AnnotationAssetManager.Asset}
	 *  @see #createAssetDescriptor(java.lang.reflect.Field, Object) */
	public <T> AssetDescriptor<T> createAssetDescriptor(Field field) {
		return createAssetDescriptor(field, null);
	}

}