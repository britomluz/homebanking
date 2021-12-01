const app = Vue.createApp({
    data(){            
        return{       
            userEmail:"",            
            userPassword:"",
            error:false,
            error_msg:"",            
            showPassword: true, 
            newuserFirstName:"",
            newuserLastName:"",
            newuserEmail:"",            
            newuserPassword:"",
            url:"",
        };
    },
    created(){
        //this.loadClient()  
        //this.login()
    },
    methods:{        
        login(mail, password){
            axios.post('/api/login',`email=${mail}&password=${password}`,
            {headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(res => {                
                if(res.status === 200){                   
                   window.location ='/web/home.html'
               }
            })
            .catch(error =>{       
                console.log(error.response.data.status)   
                      
                this.error = true;  
               // this.error_msg = error.response.data  

                if(error.response.data.status === 409){                   
                    this.error_msg = "Ya existe una sesión activa"
                }      
                if(error.response.data.status === 401){                   
                    this.error_msg = "Algo salió mal, vuelve a ingresar tu usuario y contraseña"
                }              
            })
            this.newuserEmail=""            
            this.newuserPassword=""
        },       
        register(){
            axios.post('/api/clients',`firstName=${this.newuserFirstName}&lastName=${this.newuserLastName}&email=${this.newuserEmail}&password=${this.newuserPassword}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})            
            .then(res=>{
                //return word[0].toUpperCase() + word.slice(1);
                console.log("Registro exitoso")                
                this.login(this.newuserEmail, this.newuserPassword) 
                this.newuserFirstName=""
                this.newuserLastName=""
                this.newuserEmail=""            
                this.newuserPassword=""
            })
            .catch(error =>{               
                this.error = true;                     
                this.error_msg = error.response.data   
                //console.log(error.response)         
                console.log(error.response.data)
            })
          // this.newuserFirstName=""
          // this.newuserLastName=""
          // this.newuserEmail=""            
           //this.newuserPassword=""
        },
        showPasswords(){
            this.showPassword = !this.showPassword
        },
        goBack() {
            window.history.back();
        },
        
    },
})
app.mount("#app");