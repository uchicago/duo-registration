/**
 *
 * @author danielyu
 */
package edu.uchicago.duo.domain;

import org.springframework.stereotype.Component;

@Component
public class DuoToken {
	String id;
	String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
