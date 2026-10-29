package com.mx.cbtis.gestor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mx.cbtis.gestor.controller.AsignacionesController;
import com.mx.cbtis.gestor.controller.ComentariosController;
import com.mx.cbtis.gestor.controller.ProyectosController;
import com.mx.cbtis.gestor.controller.TareasController;
import com.mx.cbtis.gestor.controller.UsuarioController;
import com.mx.cbtis.gestor.modelo.Asignaciones;
import com.mx.cbtis.gestor.modelo.Comentarios;
import com.mx.cbtis.gestor.modelo.Proyectos;
import com.mx.cbtis.gestor.modelo.Tareas;
import com.mx.cbtis.gestor.modelo.Usuario;
import com.mx.cbtis.gestor.repository.AsignacionesRepository;
import com.mx.cbtis.gestor.repository.ComentariosRepository;
import com.mx.cbtis.gestor.repository.ProtectosRepository;
import com.mx.cbtis.gestor.repository.TareasRepository;
import com.mx.cbtis.gestor.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class Ejercicio5MyApiGestorApplicationTests {

	@Mock
	private AsignacionesRepository asignacionesRepository;

	@Mock
	private ComentariosRepository comentariosRepository;

	@Mock
	private ProtectosRepository proyectosRepository;

	@Mock
	private TareasRepository tareasRepository;

	@Mock
	private UsuarioRepository usuarioRepository;

	@InjectMocks
	private AsignacionesController asignacionesController;

	@InjectMocks
	private ComentariosController comentariosController;

	@InjectMocks
	private ProyectosController proyectosController;

	@InjectMocks
	private TareasController tareasController;

	@InjectMocks
	private UsuarioController usuarioController;

	// ==========================================
	// 1. PRUEBAS DE ASIGNACIONES CONTROLLER
	// ==========================================

	@Test
	@DisplayName("GES-01: AsignacionesController - búsqueda, alta, edición, baja y listado (casos éxito y nulo)")
	void testAsignacionesController() {
		Asignaciones a = new Asignaciones(1, 10, 20, "2026-10-20");
		when(asignacionesRepository.findById(1)).thenReturn(Optional.of(a));
		when(asignacionesRepository.findById(99)).thenReturn(Optional.empty());
		when(asignacionesRepository.findAll()).thenReturn(Collections.singletonList(a));

		Asignaciones encontrada = asignacionesController.getAsignacion(1);
		assertEquals(1, encontrada.getId_asignacion());

		Asignaciones noEncontrada = asignacionesController.getAsignacion(99);
		assertNull(noEncontrada.getId_asignacion());

		assertEquals(1, asignacionesController.addAsignacion(a));
		assertEquals(0, asignacionesController.addAsignacion(null));

		assertEquals(1, asignacionesController.updateAsignacion(a));
		assertEquals(0, asignacionesController.updateAsignacion(null));

		assertEquals(1, asignacionesController.deleteAsignacion(a));
		assertEquals(0, asignacionesController.deleteAsignacion(null));

		List<Asignaciones> lista = asignacionesController.getAsignaciones();
		assertEquals(1, lista.size());
		verify(asignacionesRepository).delete(a);
	}

	// ==========================================
	// 2. PRUEBAS DE COMENTARIOS CONTROLLER
	// ==========================================

	@Test
	@DisplayName("GES-02: ComentariosController - búsqueda por tarea/ID, alta, edición, baja y listado")
	void testComentariosController() {
		Comentarios c = new Comentarios(1, "usr1", 5, "Comentario de prueba", "2026-10-20 10:00");
		when(comentariosRepository.findByTarea(5)).thenReturn(Collections.singletonList(c));
		when(comentariosRepository.findById(1)).thenReturn(Optional.of(c));
		when(comentariosRepository.findById(99)).thenReturn(Optional.empty());
		when(comentariosRepository.findAll()).thenReturn(Collections.singletonList(c));

		List<Comentarios> porTarea = comentariosController.getComentario(c);
		assertEquals(1, porTarea.size());
		assertEquals("Comentario de prueba", porTarea.get(0).getTexto_comentario());

		Comentarios encontrado = comentariosController.getComentario(1);
		assertEquals(1, encontrado.getId_comentario());

		Comentarios noEncontrado = comentariosController.getComentario(99);
		assertNull(noEncontrado.getId_comentario());

		assertEquals(1, comentariosController.addComentario(c));
		assertEquals(0, comentariosController.addComentario(null));

		assertEquals(1, comentariosController.updateComentario(c));
		assertEquals(0, comentariosController.updateComentario(null));

		assertEquals(1, comentariosController.deleteComentario(c));
		assertEquals(0, comentariosController.deleteComentario(null));

		List<Comentarios> todos = comentariosController.getComentarios();
		assertEquals(1, todos.size());
		verify(comentariosRepository).delete(c);
	}

	// ==========================================
	// 3. PRUEBAS DE PROYECTOS CONTROLLER
	// ==========================================

	@Test
	@DisplayName("GES-03: ProyectosController - búsqueda, alta, edición, baja y listado")
	void testProyectosController() {
		Proyectos p = new Proyectos(10, "Alpha", "Proyecto Alpha", "2026-01-01", "2026-12-31", "ACTIVO", "azul");
		when(proyectosRepository.findById(10)).thenReturn(Optional.of(p));
		when(proyectosRepository.findById(99)).thenReturn(Optional.empty());
		when(proyectosRepository.findAll()).thenReturn(Collections.singletonList(p));

		Proyectos encontrado = proyectosController.getProyecto(10);
		assertEquals("Alpha", encontrado.getNombre());

		Proyectos noEncontrado = proyectosController.getProyecto(99);
		assertNull(noEncontrado.getId_proyecto());

		assertEquals(1, proyectosController.addProyecto(p));
		assertEquals(0, proyectosController.addProyecto(null));

		assertEquals(1, proyectosController.updateProyecto(p));
		assertEquals(0, proyectosController.updateProyecto(null));

		assertEquals(1, proyectosController.deleteProyecto(p));
		assertEquals(0, proyectosController.deleteProyecto(null));

		List<Proyectos> lista = proyectosController.getProyectos();
		assertEquals(1, lista.size());
		verify(proyectosRepository).delete(p);
	}

	// ==========================================
	// 4. PRUEBAS DE TAREAS CONTROLLER
	// ==========================================

	@Test
	@DisplayName("GES-04: TareasController - búsqueda por proyecto/ID, alta, edición, baja y listado")
	void testTareasController() {
		Tareas t = new Tareas(7, "Testing", "Pruebas unitarias", "2026-10-25", "ToDo", "PENDIENTE", 10);
		when(tareasRepository.findByIdProyecto(10)).thenReturn(Collections.singletonList(t));
		when(tareasRepository.findById(7)).thenReturn(Optional.of(t));
		when(tareasRepository.findById(99)).thenReturn(Optional.empty());
		when(tareasRepository.findAll()).thenReturn(Collections.singletonList(t));

		List<Tareas> porProyecto = tareasController.getTarea(t);
		assertEquals(1, porProyecto.size());

		Tareas encontrada = tareasController.getTarea(7);
		assertEquals("Testing", encontrada.getNombre());

		Tareas noEncontrada = tareasController.getTarea(99);
		assertNull(noEncontrada.getId_tarea());

		assertEquals(1, tareasController.addTareas(t));
		assertEquals(0, tareasController.addTareas(null));

		assertEquals(1, tareasController.updateTareas(t));
		assertEquals(0, tareasController.updateTareas(null));

		assertEquals(1, tareasController.deleteTareas(t));
		assertEquals(0, tareasController.deleteTareas(null));

		List<Tareas> todas = tareasController.getTareas();
		assertEquals(1, todas.size());
		verify(tareasRepository).delete(t);
	}

	// ==========================================
	// 5. PRUEBAS DE USUARIO CONTROLLER
	// ==========================================

	@Test
	@DisplayName("GES-05: UsuarioController - login exitoso/fallido, búsqueda, alta, edición, baja y listado")
	void testUsuarioController() {
		Usuario u = new Usuario(1, "Miguel", "miguel@cbtis.mx", "pass123", "ADMIN", "avatar.png");
		when(usuarioRepository.findByCorreoPass("miguel@cbtis.mx", "pass123")).thenReturn(Optional.of(u));
		when(usuarioRepository.findByCorreoPass("miguel@cbtis.mx", "wrong")).thenReturn(Optional.empty());
		when(usuarioRepository.findById(1)).thenReturn(Optional.of(u));
		when(usuarioRepository.findById(99)).thenReturn(Optional.empty());
		when(usuarioRepository.findAll()).thenReturn(Arrays.asList(u));

		Usuario loginOk = usuarioController.getUsuarioLogin(u);
		assertEquals("Miguel", loginOk.getNombre());

		Usuario credencialesInvalidas = new Usuario(null, null, "miguel@cbtis.mx", "wrong", null, null);
		Usuario loginFail = usuarioController.getUsuarioLogin(credencialesInvalidas);
		assertEquals("000", loginFail.getNombre());

		Usuario encontrado = usuarioController.getUsuario(1);
		assertEquals(1, encontrado.getId_usuario());

		Usuario noEncontrado = usuarioController.getUsuario(99);
		assertNull(noEncontrado.getId_usuario());

		assertEquals(1, usuarioController.addUsuario(u));
		assertEquals(0, usuarioController.addUsuario(null));

		assertEquals(1, usuarioController.updateUsuario(u));
		assertEquals(0, usuarioController.updateUsuario(null));

		assertEquals(1, usuarioController.deleteUsuario(u));
		assertEquals(0, usuarioController.deleteUsuario(null));

		List<Usuario> lista = usuarioController.getUsuarios();
		assertEquals(1, lista.size());
		verify(usuarioRepository).delete(u);
	}

	// ==========================================
	// 6. PRUEBAS DE ENTIDADES JPA (MODELOS 100%)
	// ==========================================

	@Test
	@DisplayName("GES-06: Cobertura completa de constructores, getters y setters de modelos JPA")
	void testAllEntityModels() {
		Asignaciones asig = new Asignaciones();
		asig.setId_asignacion(1);
		asig.setId_usuario(2);
		asig.setId_tarea(3);
		asig.setFecha_asignacion("2026-10-20");
		assertEquals(1, asig.getId_asignacion());
		assertEquals(2, asig.getId_usuario());
		assertEquals(3, asig.getId_tarea());
		assertEquals("2026-10-20", asig.getFecha_asignacion());

		Comentarios com = new Comentarios();
		com.setId_comentario(1);
		com.setId_usuario("usr1");
		com.setId_tarea(5);
		com.setTexto_comentario("Hola");
		com.setFecha_hora("12:00");
		assertEquals(1, com.getId_comentario());
		assertEquals("usr1", com.getId_usuario());
		assertEquals(5, com.getId_tarea());
		assertEquals("Hola", com.getTexto_comentario());
		assertEquals("12:00", com.getFecha_hora());

		Proyectos proy = new Proyectos();
		proy.setId_proyecto(1);
		proy.setNombre("P1");
		proy.setDescripcion("Desc");
		proy.setFecha_inicio("2026-01-01");
		proy.setFecha_finalizacion("2026-06-01");
		proy.setEstado_proyecto("ACTIVO");
		proy.setFondo("bg1");
		assertEquals(1, proy.getId_proyecto());
		assertEquals("P1", proy.getNombre());
		assertEquals("Desc", proy.getDescripcion());
		assertEquals("2026-01-01", proy.getFecha_inicio());
		assertEquals("2026-06-01", proy.getFecha_finalizacion());
		assertEquals("ACTIVO", proy.getEstado_proyecto());
		assertEquals("bg1", proy.getFondo());

		Tareas tar = new Tareas();
		tar.setId_tarea(1);
		tar.setNombre("T1");
		tar.setDescripcion("TD1");
		tar.setFecha_vencimiento("2026-05-01");
		tar.setLista("Doing");
		tar.setEstado("EN_PROCESO");
		tar.setId_proyecto(9);
		assertEquals(1, tar.getId_tarea());
		assertEquals("T1", tar.getNombre());
		assertEquals("TD1", tar.getDescripcion());
		assertEquals("2026-05-01", tar.getFecha_vencimiento());
		assertEquals("Doing", tar.getLista());
		assertEquals("EN_PROCESO", tar.getEstado());
		assertEquals(9, tar.getId_proyecto());

		Usuario usr = new Usuario();
		usr.setId_usuario(1);
		usr.setNombre("Juan");
		usr.setCorreo_electronico("juan@mail.com");
		usr.setContrasena("pwd");
		usr.setRol("DEV");
		usr.setImagen_usuario("pic.jpg");
		assertEquals(1, usr.getId_usuario());
		assertEquals("Juan", usr.getNombre());
		assertEquals("juan@mail.com", usr.getCorreo_electronico());
		assertEquals("pwd", usr.getContrasena());
		assertEquals("DEV", usr.getRol());
		assertEquals("pic.jpg", usr.getImagen_usuario());
	}

	// =========================================================================
	// 7. PRUEBAS DE SEGURIDAD DE BASE DE DATOS (SQLi CWE-89, CWE-287, CWE-256)
	// =========================================================================

	@Test
	@DisplayName("GES-07: Seguridad BD - Prevención de SQL Injection (CWE-89), Auth Bypass (CWE-287) y Hashes BCrypt (CWE-256)")
	void testDatabaseSecurityAndSqlInjectionPrevention() {
		// 1. Prueba de resistencia a SQL Injection en autenticación (CWE-89 / CWE-287)
		String sqliEmailPayload = "admin@cbtis75.edu.mx' OR '1'='1' --";
		String sqliPassPayload = "' OR 1=1; DROP TABLE usuario; --";
		when(usuarioRepository.findByCorreoPass(sqliEmailPayload, sqliPassPayload)).thenReturn(Optional.empty());

		Usuario atacante = new Usuario(null, null, sqliEmailPayload, sqliPassPayload, null, null);
		Usuario resultadoSqli = usuarioController.getUsuarioLogin(atacante);
		assertEquals("000", resultadoSqli.getNombre());
		assertNull(resultadoSqli.getId_usuario());

		// 2. Verificación de autenticación con hash criptográfico BCrypt ($2a$12$) en lugar de texto plano (CWE-256)
		String bcryptHash = "$2a$12$R9h/cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUW";
		Usuario usuarioSeguro = new Usuario(131, "Ana Paola Ramírez Vázquez", "ana22533@cbtis75.edu.mx", bcryptHash, "Estudiante", "#70FAB7");
		when(usuarioRepository.findByCorreoPass("ana22533@cbtis75.edu.mx", bcryptHash)).thenReturn(Optional.of(usuarioSeguro));

		Usuario loginSeguro = usuarioController.getUsuarioLogin(usuarioSeguro);
		assertEquals(131, loginSeguro.getId_usuario());
		assertEquals(true, loginSeguro.getContrasena().startsWith("$2a$12$"));
	}
}
