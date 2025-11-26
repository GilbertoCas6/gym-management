// ============================================
// GESTION-CLASES.JS
// JavaScript para el módulo de gestión de clases
// ============================================

// Función para contar entrenadores únicos en la tabla
function actualizarEstadisticas() {
    const rows = document.querySelectorAll('tbody tr');
    const entrenadoresSet = new Set();

    rows.forEach(row => {
        const entrenadorCell = row.querySelector('td:nth-child(3)');
        if (entrenadorCell && entrenadorCell.textContent.trim() !== 'Sin asignar') {
            entrenadoresSet.add(entrenadorCell.textContent.trim());
        }
    });

    document.getElementById('totalEntrenadores').textContent = entrenadoresSet.size;
}

// Sincronizar checkbox de CREAR con campo hidden
function inicializarCheckboxCrear() {
    const checkbox = document.getElementById('createActiva');
    const hidden = document.getElementById('createActivaHidden');

    if (checkbox && hidden) {
        checkbox.addEventListener('change', function() {
            hidden.value = this.checked ? 'true' : 'false';
        });
    }
}

// Sincronizar checkbox de EDITAR con campo hidden
function inicializarCheckboxEditar() {
    const checkbox = document.getElementById('editActiva');
    const hidden = document.getElementById('editActivaHidden');

    if (checkbox && hidden) {
        checkbox.addEventListener('change', function() {
            hidden.value = this.checked ? 'true' : 'false';
        });
    }
}

// Editar clase usando data attributes
function inicializarBotonesEditar() {
    document.querySelectorAll('.btn-editar').forEach(btn => {
        btn.addEventListener('click', function() {
            const isActiva = this.dataset.activa === 'true';

            document.getElementById('editId').value = this.dataset.id;
            document.getElementById('editNombre').value = this.dataset.nombre;
            document.getElementById('editDescripcion').value = this.dataset.descripcion || '';
            document.getElementById('editIdEntrenador').value = this.dataset.entrenador || '';
            document.getElementById('editDiaSemana').value = this.dataset.dia;
            document.getElementById('editHoraInicio').value = this.dataset.hora;
            document.getElementById('editDuracion').value = this.dataset.duracion;
            document.getElementById('editCapacidadMaxima').value = this.dataset.capacidad;
            document.getElementById('editActiva').checked = isActiva;
            document.getElementById('editActivaHidden').value = isActiva ? 'true' : 'false';
            document.getElementById('formEditarClase').action = '/admin/clases/editar/' + this.dataset.id;
        });
    });
}

// Eliminar clase usando data attributes
function inicializarBotonesEliminar() {
    document.querySelectorAll('.btn-eliminar').forEach(btn => {
        btn.addEventListener('click', function() {
            document.getElementById('deleteNombre').textContent = this.dataset.nombre;
            document.getElementById('formEliminarClase').action = '/admin/clases/eliminar/' + this.dataset.id;
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
    inicializarCheckboxCrear();
    inicializarCheckboxEditar();
    inicializarBotonesEditar();
    inicializarBotonesEliminar();
    inicializarAlertas();
});