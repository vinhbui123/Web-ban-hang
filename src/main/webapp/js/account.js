// Hàm để kích hoạt chỉnh sửa trường nhập liệu
function toggleEdit(fieldId) {
    const field = document.getElementById(fieldId);
    const isDisabled = field.disabled;

    // Chuyển đổi trạng thái disabled
    field.disabled = !isDisabled;

    // Focus vào trường nhập liệu khi nó được kích hoạt
    if (!field.disabled) {
        field.focus();
    }

    document.getElementById('avatarUpload').addEventListener('change', function () {
        const file = this.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function (e) {
                document.getElementById('avatarImage').src = e.target.result;
            };
            reader.readAsDataURL(file);
        }
    });

}

function previewAvatar(input) {
    const avatarImage = document.getElementById('avatarImage');
    const avatarPreview = document.getElementById('avatarPreview');

    if (input.files && input.files[0]) {
        const reader = new FileReader();

        reader.onload = function(e) {
            avatarImage.src = e.target.result;
        }

        reader.readAsDataURL(input.files[0]);
    }
}
