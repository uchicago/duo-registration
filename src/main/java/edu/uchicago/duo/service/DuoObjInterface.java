/**
 * Copyright 2014 University of Chicago
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * Author: Daniel Yu <danielyu@uchicago.edu>
 */
package edu.uchicago.duo.service;

import edu.uchicago.duo.domain.DuoPhone;
import edu.uchicago.duo.domain.DuoTablet;
import edu.uchicago.duo.domain.DuoToken;
import java.util.List;
import java.util.Map;

public interface DuoObjInterface {

	public String getObjById();

	public String getObjStatusById(String param1);
	
	public String getObjByParam(String param1, String param2, String attribute);

	public String createObjByParam(String param1, String param2, String param3, String param4, String param5);

	public void deleteObj(String param1, String param2);

	public void associateObjs(String param1, String param2);

	public String objActionById(String param1, String param2);

	public void resyncObj(String param1, String param2, String param3, String param4);
	
	public Map<String, Object> verifyObj(String param1, String param2, String param3); 

	public List<DuoPhone> getAllPhones(String param1);

	public List<DuoTablet> getAllTablets(String param1);

	public List<DuoToken> getAllTokens(String param1);
}
