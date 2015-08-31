package mk.finki.webprogramiranje.projectmanager.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import mk.finki.webprogramiranje.projectmanager.model.Member;
import mk.finki.webprogramiranje.projectmanager.service.MemberService;

@RestController
@RequestMapping(value="/members")
public class MemberController {
	@Autowired
	private MemberService service;
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<Member> getById(HttpSession session, @PathVariable String id){
		Member member = service.findById(id);
		if(member != null){
			member.setEmail(null);
			member.setPassword(null);
			
			return new ResponseEntity<Member>(member, HttpStatus.OK);
		}else{
			return new ResponseEntity<Member>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value="/email", method=RequestMethod.POST)
	public ResponseEntity<Member> getByEmail(HttpSession session, @RequestParam String email){
		String sessionId = (String)session.getAttribute("id");
		if(sessionId != null){
			Member member = service.findByEmail(email);
			if(member != null){
				member.setEmail(null);
				member.setPassword(null);
				
				return new ResponseEntity<Member>(member, HttpStatus.OK);
			}else{
				return new ResponseEntity<Member>(HttpStatus.NOT_FOUND);
			}
		}else{
			return new ResponseEntity<Member>(HttpStatus.UNAUTHORIZED);
		}
	}
	
	
	@RequestMapping(value="/search", method=RequestMethod.GET)
	public ResponseEntity<List<Member>> getSearchByEmail(HttpSession session, @RequestParam String email){
		String sessionId = (String)session.getAttribute("id");
		if(sessionId != null){
			List<Member> member = service.searchByEmail(email);
			if(member != null){
				
				Member rem = null;
				for(Member m : member){
					m.setEmail(null);
					m.setPassword(null);
					//test pic
					m.setPicture("app/imgs/signin/user.png");
					if(m.getId().equals(sessionId)){
						rem = m;
					}
				}
				if(rem != null){
					member.remove(rem);
				}
				
				return new ResponseEntity<List<Member>>(member, HttpStatus.OK);
			}else{
				return new ResponseEntity<List<Member>>(HttpStatus.NOT_FOUND);
			}
		}else{
			return new ResponseEntity<List<Member>>(HttpStatus.UNAUTHORIZED);
		}
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Void> update(HttpSession session, @PathVariable String id, @RequestBody @Valid Member jsonMember){
		String sessionId = (String)session.getAttribute("id");
		if(sessionId != null && sessionId.equals(id)){
			if(jsonMember.getId().equals(sessionId)){
				service.save(jsonMember);
				return new ResponseEntity<Void>(HttpStatus.OK);
			}else{
				return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
			}
		}else{
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}
	}
	
	@RequestMapping(value="/{id}/add-picture", method=RequestMethod.POST)
	public ResponseEntity<Member> changePicture(HttpSession session, @PathVariable String id, @RequestParam MultipartFile picture){
		String sessionId = (String)session.getAttribute("id");
		if(sessionId != null && sessionId.equals(id)){
			if(!picture.isEmpty() && (picture.getContentType().equals("image/jpeg") || picture.getContentType().equals("image/png"))){
				Member member = service.findById(sessionId);
				if(service.savePicture(member, picture)){
					return new ResponseEntity<Member>(HttpStatus.OK);
				}else{
					return new ResponseEntity<Member>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}else{
				return new ResponseEntity<Member>(HttpStatus.BAD_REQUEST);
			}
		}else{
			return new ResponseEntity<Member>(HttpStatus.UNAUTHORIZED);
		}
	}
	
	@RequestMapping(value="/{id}/remove-picture", method=RequestMethod.GET)
	public ResponseEntity<Member> removePicture(HttpSession session, @PathVariable String id){
		String sessionId = (String)session.getAttribute("id");
		if(sessionId != null && sessionId.equals(id)){
			Member member = service.findById(sessionId);
			
			if(service.removePicture(member)){
				member.setEmail("");
				member.setPassword("");
				
				return new ResponseEntity<Member>(member, HttpStatus.OK);
			}else{
				return new ResponseEntity<Member>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}else{
			return new ResponseEntity<Member>(HttpStatus.UNAUTHORIZED);
		}
	}
}