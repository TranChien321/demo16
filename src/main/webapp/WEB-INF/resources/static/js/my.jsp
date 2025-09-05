$(document).ready(function () {
// ma nguon jquery

$(".image-user").click(function () {
const urlImage = $(this).attr("src")
$("#image-show").attr("src", urlImage);
$("#myModal").css("display", "block");
})

$(".close-modal-image-show").click(function (){
$("#myModal").css("display", "none");
})

const currentLang = getCookie("Locale");
renderOptionLang(currentLang);
const showUserSearch = (users) => {
let html = "";
users.forEach((item, index) => {
html += '<tr>';
html += `<td>${index+1}</td>`
html += `<td>${item.username}</td>`
html += `<td><img width="150" src="/resources/uploads/${item.imageUrl}"></td>`
html += `<td>${item.email}</td>`
html += `<td>${item.phone}</td>`
html += `<td>${item.departmentName}</td>`
html += `
<td>
    <a class="btn btn-danger" onclick="return confirm('Are you sure')" href="/users/${item.id}/delete">
        <i class="fa-solid fa-trash"></i>
    </a>
    <a class="btn btn-primary" href="/users/${item.id}/edit">
        <i class="fa-solid fa-pen-to-square"></i>
    </a>
</td>`
html += '</tr>';
})

$("#list-user").html(html)
}

$("#input-search-user").keyup(function () {
const value = $(this).val();
$.ajax({
url: "/admin/users/search",
method: "GET",
data: {
keyword: value
},
success: function( result) {
console.log(result)
showUserSearch(result.users)
},
error: function (err) {

}
});
})

// lay cookie LOCALE
$("#select-lang").change(() => {
$("#form-change-lang").submit();
})

})

function renderOptionLang(currentLang) {
let html = "";
html += `<option ${currentLang === 'en' ? 'selected': '' }" value="en">EN</option>
<option ${currentLang === 'vi' ? 'selected': '' } value="vi">VN</option>`;

$("#select-lang").html(html)

}

function getCookie(name) {
const nameEQ = name + "=";
const ca = document.cookie.split(';'); // Split all cookies into an array
for (let i = 0; i < ca.length; i++) {
let c = ca[i];
while (c.charAt(0) === ' ') { // Remove leading spaces
c = c.substring(1, c.length);
}
if (c.indexOf(nameEQ) === 0) { // Check if the cookie starts with the desired name
return decodeURIComponent(c.substring(nameEQ.length, c.length)); // Return the decoded value
}
}
return null; // Return null if the cookie is not found
}