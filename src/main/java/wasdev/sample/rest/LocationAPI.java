/*******************************************************************************
 * Copyright (c) 2017 IBM Corp.
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
 *******************************************************************************/
package wasdev.sample.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;

import com.google.gson.Gson;

import wasdev.sample.Location;
import wasdev.sample.store.LocationStore;
import wasdev.sample.store.LocationStoreFactory;

@ApplicationPath("api")
@Path("/locations")
public class LocationAPI extends Application {

	//Our database store
	LocationStore store = LocationStoreFactory.getInstance();

  /**
   * Gets all Location.
   * REST API example:
   * <code>
   * GET http://localhost:9080/LibertyOutageReporter/api/locations
   * </code>
   *
   * Response:
   * <code>
   * [ "Seattle", "Austin" ]
   * </code>
   * @return A collection of all the Locations
   */
    @GET
    @Path("/")
    @Produces({"application/json"})
    public String getLocations() {

		if (store == null) {
			return "[]";
		}

		List<String> names = new ArrayList<String>();
		for (Location doc : store.getAll()) {
			String name = doc.getName();
			if (name != null){
				names.add(name);
			}
		}
		return new Gson().toJson(names);
    }

    /**
     * Creates a new Location.
     *
     * REST API example:
     * <code>
     * POST http://localhost:9080/LibertyOutageReporter/api/locations
     * <code>
     * POST Body:
     * <code>
     * {
     *   "name":"Seattle"
     * }
     * </code>
     * Response:
     * <code>
     * {
     *   "id":"123",
     *   "name":"Seattle"
     * }
     * </code>
     * @param location The new Location to create.
     * @return The Location after it has been stored.  This will include a unique ID for the Location.
     */
    @POST
    @Produces("application/text")
    @Consumes("application/json")
    public String newToDo(Location location) {
      if(store == null) {
    	  return String.format("Outage in %s reported!", location.getName());
      }
      store.persist(location);
      return String.format("Outage in %s has been added to the database.", location.getName());

    }

    @DELETE
    @Produces("application/text")
    @Consumes("application/text")
    public String newFoo(String toRemove) {
      if(store == null) {
          return String.format("Outage in %s cleared!", toRemove);
      }
      for (Location doc : store.getAll()) {
          String name = doc.getName();
          if (name.equals(toRemove)){
              store.delete(doc);
          }
      }
      //store.delete(location);
      return String.format("Outage in %s has been cleared!", toRemove);

    }

}
