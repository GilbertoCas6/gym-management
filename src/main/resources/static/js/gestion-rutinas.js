// ============================================
// GESTION-RUTINAS.JS
// ============================================

document.addEventListener('DOMContentLoaded', function () {

    // EDITAR RUTINA: llenar el modal con data attributes
    document.querySelectorAll('.btn-editar-rutina').forEach(btn => {
        btn.addEventListener('click', function () {

            document.getElementById('editId').value = this.dataset.id || '';
            document.getElementById('editNombre').value = this.dataset.nombre || '';
            document.getElementById('editObjetivo').value = this.dataset.objetivo || '';
            document.getElementById('editCliente').value = this.dataset.idcliente || '';
            document.getElementById('editEntrenador').value = this.dataset.identrenador || '';
            document.getElementById('editDias').value = this.dataset.dias || '';
            document.getElementById('editSemanas').value = this.dataset.semanas || '';
            document.getElementById('editDescripcion').value = this.dataset.descripcion || '';

            const activa = this.dataset.activa === 'true';
            const chk = document.getElementById('editActiva');
            const hidden = document.getElementById('editActivaHidden');

            if (chk && hidden) {
                chk.checked = activa;
                hidden.value = activa ? 'true' : 'false';
            }

            const form = document.getElementById('formEditarRutina');
            if (form) {
                form.action = '/admin/rutinas/editar/' + this.dataset.id;
            }
        });
    });

    // Sincronizar checkbox con hidden
    const chkEdit = document.getElementById('editActiva');
    const hiddenEdit = document.getElementById('editActivaHidden');

    if (chkEdit && hiddenEdit) {
        chkEdit.addEventListener('change', function () {
            hiddenEdit.value = this.checked ? 'true' : 'false';
        });
    }
});
