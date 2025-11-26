// ============================================
// GESTION-USUARIOS.JS
// JavaScript para el módulo de gestión de usuarios
// ============================================

// Contar roles para estadísticas
function actualizarEstadisticas() {
    const rows = document.querySelectorAll('tbody tr');
    let clientes = 0;
    let entrenadores = 0;

    rows.forEach(row => {
        const rolBadge = row.querySelector('td:nth-child(5)');
        if (rolBadge) {
            if (rolBadge.textContent.includes('CLIENTE')) clientes++;
            if (rolBadge.textContent.includes('ENTRENADOR')) entrenadores++;
        }
    });

    document.getElementById('totalClientes').textContent = clientes;
    document.getElementById('totalEntrenadores').textContent = entrenadores;
}

// Mostrar/ocultar campos según rol (CREAR)
function inicializarCamposRolCrear() {
    const rolSelect = document.getElementById('rolSelectCrear');
    if (!rolSelect) return;

    rolSelect.addEventListener('change', function() {
        const rol = this.value;
        const campoFecha = document.getElementById('campoFechaIngresoCrear');
        const campoObjetivo = document.getElementById('campoObjetivoCrear');
        const campoEntrenador = document.getElementById('campoEntrenadorCrear');

        if (rol === 'CLIENTE') {
            campoFecha.style.display = 'block';
            campoObjetivo.style.display = 'block';
            campoEntrenador.style.display = 'block';
        } else {
            campoFecha.style.display = 'none';
            campoObjetivo.style.display = 'none';
            campoEntrenador.style.display = 'none';
        }
    });
}

// Editar usuario usando data attributes
function inicializarBotonesEditar() {
    document.querySelectorAll('.btn-editar').forEach(btn => {
        btn.addEventListener('click', function() {
            document.getElementById('editId').value = this.dataset.id;
            document.getElementById('editNombre').value = this.dataset.nombre;
            document.getElementById('editEmail').value = this.dataset.email;
            document.getElementById('editTelefono').value = this.dataset.telefono || '';
            document.getElementById('editRol').value = this.dataset.rol;
            document.getElementById('editActivo').checked = this.dataset.activo === 'true';
            document.getElementById('editFechaIngreso').value = this.dataset.fecha || '';
            document.getElementById('editObjetivo').value = this.dataset.objetivo || '';
            document.getElementById('editIdEntrenador').value = this.dataset.entrenador || '';
            document.getElementById('formEditarUsuario').action = '/admin/usuarios/editar/' + this.dataset.id;

            // Mostrar/ocultar campos según rol
            const rol = this.dataset.rol;
            const campoFecha = document.getElementById('campoFechaIngresoEditar');
            const campoObjetivo = document.getElementById('campoObjetivoEditar');
            const campoEntrenador = document.getElementById('campoEntrenadorEditar');

            if (rol === 'CLIENTE') {
                campoFecha.style.display = 'block';
                campoObjetivo.style.display = 'block';
                campoEntrenador.style.display = 'block';
            } else {
                campoFecha.style.display = 'none';
                campoObjetivo.style.display = 'none';
                campoEntrenador.style.display = 'none';
            }
        });
    });
}

// Eliminar usuario usando data attributes
function inicializarBotonesEliminar() {
    document.querySelectorAll('.btn-eliminar').forEach(btn => {
        btn.addEventListener('click', function() {
            document.getElementById('deleteNombre').textContent = this.dataset.nombre;
            document.getElementById('formEliminarUsuario').action = '/admin/usuarios/eliminar/' + this.dataset.id;
        });
    });
}

// Auto-hide alerts después de 5 segundos
function inicializarAlertas() {
    setTimeout(function() {
        const alerts = document.querySelectorAll('.alert');
        alerts.forEach(alert => {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        });
    }, 5000);
}

// Inicializar todo cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', function() {
    actualizarEstadisticas();
    inicializarCamposRolCrear();
    inicializarBotonesEditar();
    inicializarBotonesEliminar();
    inicializarAlertas();
});