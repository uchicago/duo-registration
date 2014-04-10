/**
 * @author Daniel Yu, danielyu@uchicago.edu
 */
package edu.uchicago.duo.web;

import edu.uchicago.duo.domain.DuoPersonObj;
import edu.uchicago.duo.service.DuoObjInterface;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("devicemgmt")
@SessionAttributes("DuoPerson")
public class DuoDeviceMgmtController {

	protected final Log logger = LogFactory.getLog(getClass());
	//
	@Autowired
	private DuoObjInterface duoUsrService;
	//
	@Autowired
	private DuoObjInterface duoPhoneService;
	//
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));

	}
	
	
	//Temporary Test Controller for Device Controller Button Submission
	@RequestMapping(method = RequestMethod.POST)
	public String processPage(@ModelAttribute("DuoPerson") DuoPersonObj duoperson,
			BindingResult result, HttpSession session, SessionStatus status, ModelMap model) throws UnsupportedEncodingException, JSONException, Exception {
				
		logger.info("Value We Got!: "+duoperson.getPhone_id());
		logger.info("Value We Got!: "+duoperson.getChoosenDevice());
		duoperson = new DuoPersonObj();
		initForm(model, duoperson, session, status);
		//return form view
		return "DuoDeviceMgmt";
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String initForm(ModelMap model, @ModelAttribute DuoPersonObj duoperson, HttpSession session, SessionStatus status) {
		String userId = null;
		String username = null;
		
		username = session.getAttribute("username").toString();

		if (session.getAttribute("duoUserId") == null) {
			userId = duoUsrService.getObjByParam(username, null, "userId");
			if (userId == null) {
				logger.info("This User:" + username + " has not yet register with DUO!");
				status.setComplete();
				return "duo";
			}
			logger.info("Assigned UserID via DUO API Query");
		} else {
			userId = session.getAttribute("duoUserId").toString();
			logger.info("Assigned UserID via Session Variable");
		}


		duoperson.setUser_id(userId);
		duoperson.setUsername(username);
		duoperson.setPhones(duoPhoneService.getAllPhones(userId));
		
		model.addAttribute("DuoPerson", duoperson);

		logger.info("Username(DevMgmt):" + duoperson.getUsername());
		logger.info("UserID(DevMgmt):" + duoperson.getUser_id());
		logger.info("PhoneID(DevMgmt):" + duoperson.getPhone_id());
		logger.info("PHONES(DevMgmt):" + duoperson.getPhones());

		//return form view
		return "DuoDeviceMgmt";
	}
}
