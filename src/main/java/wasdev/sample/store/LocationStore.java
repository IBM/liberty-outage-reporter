/*
 * Copyright IBM Corp. 2017
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wasdev.sample.store;

import java.util.Collection;

import com.cloudant.client.api.Database;

import wasdev.sample.Location;

/**
 * Defines the API for a ToDo store.
 *
 */
public interface LocationStore {

  	/**
	 * Get the target db object.
	 *
	 * @return Database.
  	 * @throws Exception
	 */
  public Database getDB();

  	/**
	 * Gets all Locations from the store.
	 *
	 * @return All Locations.
  	 * @throws Exception
	 */
  public Collection<Location> getAll();

  /**
   * Gets an individual ToDo from the store.
   * @param id The ID of the ToDo to get.
   * @return The ToDo.
   */
  public Location get(String id);

  /**
   * Persists a Location to the store.
   * @param td The ToDo to persist.
   * @return The persisted ToDo.  The ToDo will not have a unique ID..
   */
  public Location persist(Location vi);

  /**
   * Updates a ToDo in the store.
   * @param id The ID of the Location to update.
   * @param td The Location with updated information.
   * @return The updated Location.
   */
  public Location update(String id, Location vi);

  /**
   * Deletes a ToDo from the store.
   * @param id The ID of the Location to delete.
   */
  public void delete(String id);

  /**
   * Counts the number of Locations
   * @return The total number of Locations.
 * @throws Exception
   */
  public int count() throws Exception;
}
