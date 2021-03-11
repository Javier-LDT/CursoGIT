package com.bolsadeideas.springboot.form.app.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.bolsadeideas.springboot.form.app.editors.NombreMayusculaEditor;
import com.bolsadeideas.springboot.form.app.editors.PaisPropertyEditor;
import com.bolsadeideas.springboot.form.app.editors.RolesEditor;
import com.bolsadeideas.springboot.form.app.models.domain.Pais;
import com.bolsadeideas.springboot.form.app.models.domain.Role;
import com.bolsadeideas.springboot.form.app.models.domain.Usuario;
import com.bolsadeideas.springboot.form.app.services.PaisService;
import com.bolsadeideas.springboot.form.app.services.RoleService;
import com.bolsadeideas.springboot.form.app.validation.UsuarioValidador;

@Controller
@SessionAttributes("usuario") // mantiene todos los campos que no se modifiquen. sin el el identoificador
								// pasaria a nully va con SessionStatus status
public class FormController {

	@Autowired
	private UsuarioValidador validador;
	
	@Autowired
	private PaisService paisService;
	
	@Autowired
	private RoleService roleService;

	@Autowired
	private PaisPropertyEditor paisEditor;
	@Autowired
	private RolesEditor roleEditor;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(validador);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// equivale a @DateTimeFormat(pattern =
																			// "yyyy-MM-dd") SimpleDateFormat cambia un
																			// eSting a date formato fecha
		dateFormat.setLenient(false);// es si es estricto o tolerante false es estricto
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));

		binder.registerCustomEditor(String.class, "nombre", new NombreMayusculaEditor());
		binder.registerCustomEditor(Pais.class, "pais", paisEditor);
		binder.registerCustomEditor(Role.class, "roles", roleEditor);
	}

	@ModelAttribute("genero")
	public List<String> genero() {
		return Arrays.asList("Hombre", "Mujer");
	}
	@ModelAttribute("listaPaises")
	public List<Pais> listaPaises(){
		return paisService.Listar();/*Arrays.asList(
				new Pais(1, "ES", "España"),
				new Pais(2, "MX","México"),
				new Pais(3, "CL","Chile"),
				new Pais(4, "BR","Brasil"),
				new Pais(5, "PR","Portugal"),
				new Pais(6, "CL","Colombia"),
				new Pais(7, "VN","Venezuela")); esto pertenece a listar pais sin el package .app.services*/
	}
	@ModelAttribute("listaRoles")
	public List<Role> listaRoles(){
		return this.roleService.listar();
	}
	
	@ModelAttribute("listaRolesString")
	public List<String> listaRolesString(){
		List<String> roles = new ArrayList<>();
		roles.add("ROLE_ADMIN");
		roles.add("ROLE_USER");
		roles.add("ROLE_MODERATOR");
		return roles;
	}
	@ModelAttribute("listaRolesMap")
	public Map<String, String> listaRolesMap() {
		Map<String, String> roles = new HashMap<String, String>();
		roles.put("ROLE_ADMIN", "Administrador");
		roles.put("ROLE_USER", "Usuario");
		roles.put("ROLE_MODERATOR", "Moderador");
		return roles;
	}


	@ModelAttribute("paises")
	public List<String> paises() {
		return Arrays.asList("España", "México", "Chile", "Brasil", "Portugal", "Colombia", "Venezuela");
	}

	@ModelAttribute("paisesMap")
	public Map<String, String> paisesMap() {
		Map<String, String> paises = new HashMap<String, String>();
		paises.put("ES", "España");
		paises.put("MX", "Mexíco");
		paises.put("CL", "Chile");
		paises.put("AR", "Argentina");
		paises.put("BR", "Brasil");
		paises.put("PR", "Colombia");
		paises.put("VN", "Venezuela");
		return paises;
	}

	@GetMapping("/form")
	public String form(Model model) {
		Usuario usuario = new Usuario();
		usuario.setNombre("Jhon");
		usuario.setApellido("Don");
		usuario.setIdentificador("123.234-K");
		usuario.setHabilitar(true);
		usuario.setValorSecreto("ALgun valor secreto*****");
		usuario.setPais(new Pais(3, "CL","Chile"));
		model.addAttribute("titulo", "Formulario usuarios");
		model.addAttribute("usuario", usuario);
		return "form";
	}

	@PostMapping("/form")
	public String procesar(@Valid Usuario usuario, BindingResult result, Model model) {
	/*
	 * @RequestParam String username,//si el nombre en html es distinto ponemos
	 * (name="username")
	 * 
	 * @RequestParam String password,
	 * 
	 * @RequestParam String email){ Usuario usuario= new Usuario();
	 * usuario.setUsername(username); usuario.setPassword(password);
	 * usuario.setEmail(email); dejo comentado el codigo ya que metiendo el objeto
	 * Usuario usuario delante del model el program hace lo mismo que con este
	 * codigo,meter los datos en los campos del formulario
	 */ 
		//validador.validate(usuario, result);
		
		if (result.hasErrors()) {
			/*
			 * con formmenosrobus con este codigoMap<String, String> errores= new
			 * HashMap<>(); result.getFieldErrors().forEach(err ->{
			 * errores.put(err.getField(),
			 * "El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()
			 * )); }); model.addAttribute("error", errores);
			 */
			model.addAttribute("titulo", "Resultado form");
			return "form";
		}
		return "redirect:/ver";
	}
	
	@GetMapping("/ver")//esto redirige a una nueva pagina impidiendo que refresque y se envien los datos varias veces
	public String ver(@SessionAttribute(name="usuario", required = false)Usuario usuario, Model model, SessionStatus status) {
		if (usuario==null) {
			return "redirect:/form";
		}
		model.addAttribute("titulo", "Resultado form");
		status.setComplete();
		return "resultado";
	}
}
