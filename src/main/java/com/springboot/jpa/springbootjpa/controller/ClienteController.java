package com.springboot.jpa.springbootjpa.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.jpa.springbootjpa.models.entity.Cliente;
import com.springboot.jpa.springbootjpa.models.services.IClienteService;
import com.springboot.jpa.springbootjpa.models.services.IUploadFileService;
import com.springboot.jpa.springbootjpa.util.PageRender;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@SessionAttributes("cliente")
public class ClienteController {

    @Autowired
    private IClienteService clienteService;
    @Autowired
    private IUploadFileService uploadFileService;

    @GetMapping(value = "/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
        Cliente cliente = clienteService.findOne(id);

        if(cliente == null) {
            flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
            return "redirect:/listar";
        }

        model.put("cliente", cliente);
        model.put("titulo", "Detalle cliente: " + cliente.getNombre());
        
        return "ver";
    }
    

    @RequestMapping(value = "/listar", method = RequestMethod.GET)
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page,  Model model) {
        Pageable pageRequest = PageRequest.of(page, 4);
        Page<Cliente> clientes = clienteService.findAll(pageRequest);

        PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("clientes", clientes);
        model.addAttribute("page", pageRender);

        return "listar";
    }

    @RequestMapping(value = "/form")
    public String crear(Map<String, Object> model) {
        Cliente cliente = new Cliente();
        model.put("cliente", cliente);
        model.put("titulo", "Formulario de cliente");

        return "form";
    }

    @RequestMapping(value = "/form/{id}")
    public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
        Cliente cliente = null;

        if (id > 0) {
            cliente = clienteService.findOne(id);
            if(cliente == null) {
                flash.addFlashAttribute("error", "El ID del cliente no existe");
                return "redirect:/listar";
            }
        } else {
            flash.addFlashAttribute("error", "El ID del cliente no puede ser cero");
            return "redirect:/listar";
        }

        model.put("cliente", cliente);
        model.put("titulo", "Editar cliente");

        return "form";
    }

    /**
     * el bindigresult debe ir junto al formulario siempre. El objeto se pasa
     * automaticamente cuando existe un error siempre y cuando el objecto que se
     * envia en el
     * metodo crear tenga el mismo nombre que la clase(no diferencia mayusculas y
     * minusculas)
     * si los nombres son diferentes se debe agregar ModelAtrribute
     */
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model, @RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de Cliente");
            return "form";
        }

        if(!foto.isEmpty()) {
            String uniqueFilename;
            try {
                uniqueFilename = uploadFileService.copy(foto);
                flash.addFlashAttribute("info", "Ha subido correctamente " + uniqueFilename);
                cliente.setFoto(uniqueFilename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String mensajeFlash = (cliente.getId() != null) ? "Cliente editado con exito" : "Cliente creado con exito";

        clienteService.save(cliente);
        status.setComplete();// elimino el objeto de la session
        flash.addFlashAttribute("success", mensajeFlash);

        return "redirect:listar";
    }

    @RequestMapping(value = "/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
        if (id > 0) {
            Cliente cliente = clienteService.findOne(id);

            clienteService.delete(id);
            flash.addFlashAttribute("success", "Cliente eliminado con exito");

            //Eliminando la foto
            if(uploadFileService.delete(cliente.getFoto())) {
                flash.addFlashAttribute("info", "Foto " + cliente.getFoto() + " eliminada con éxito");
            }
        }      

        return "redirect:/listar";
    }

}
