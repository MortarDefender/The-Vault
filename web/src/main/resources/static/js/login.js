const signInBtn = document.getElementById("signIn");
const signUpBtn = document.getElementById("signUp");
const firstForm = document.getElementById("form1");
const secondForm = document.getElementById("form2");
const container = document.querySelector(".container");
const loginEye = document.getElementById("loginEye");
const registerEye = document.getElementById("registerEye");
const loginPassword = document.getElementById("loginPassword");
const registerPassword = document.getElementById("registerPassword");

signInBtn.addEventListener("click", () => {
	container.classList.remove("right-panel-active");
});

signUpBtn.addEventListener("click", () => {
	container.classList.add("right-panel-active");
});

// firstForm.addEventListener("submit", (e) => e.preventDefault());   // function to create a new user
// secondForm.addEventListener("submit", (e) => e.preventDefault());  // function to login with an old user


loginEye.addEventListener("click", () => {
	maskUnMasked("loginEye", loginPassword);
});

registerEye.addEventListener("click", () => {
	maskUnMasked("registerEye", registerPassword);
});


function maskUnMasked(eye, passwordField) {
	if (passwordField.type === "password") {
		passwordField.type = "text";
		$('#' + eye).removeClass().addClass('fas fa-eye-slash');
	} else {
		passwordField.type = "password";
		$('#' + eye).removeClass().addClass('fas fa-eye');
	}
}
