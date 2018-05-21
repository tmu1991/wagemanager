var person=JSON.parse(localStorage.dome);
var app=new Vue({
	el:'#app',
	data:person,
	methods:{
		console.log(this.person);
	}
})
