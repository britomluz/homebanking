const app = Vue.createApp({
    data(){
        return{
            clientes:[],
            client:[],
            accounts:[],
            cards:[],
            loans:[],
            loanss:[],
            search: '',
            //add client
            firstName: '',
            lastName: '',
            email: '',
            url: '',
            password:'',
            role: '',
            errorC:false,
            msg_addClient:"",

            //delete acc
            errorA:false,
            msg_delAccount:"",
            errorL: false,                     
            msg_newLoan:"",

            //create loan
            name:"",
            maxAmount:"",
            payments:[],
            interest:"",

            //edit client
            newFirstName: '',
            newLastName: '',
            newEmail: '',
            json:"", 
            selectedClients:[],
            deleteClients:[],
            modifyClients:[],           
            modifyC:false,
            deleteC:false,
            radioButton:false,
            searchClients:false
        }
    },
    created(){
        this.loadClient()     
        this.loadDataClient()  
        this.loadLoans()

    },
    methods:{       
        loadClient(){
            axios.get("/api/clients")
            .then(res => {                
                this.clientes = res.data.sort((a,b) => parseInt(a.id - b.id))                  
                console.log(res.data)      
            })
            .catch(err => console.error(err.message))
        },
        loadLoans(){
            axios.get("/api/loans")
            .then(res => {                
                
                this.loanss= res.data.sort((a,b) => parseInt(a.id - b.id))
                console.log(this.loanss)      
            })
            .catch(err => console.error(err.message))
        },
        addClient() {                       
            axios.post('/api/admin/clients',`firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}&password=${'clientNew0405'}&rol=${this.role}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})            
            .then(res=>{
                //return word[0].toUpperCase() + word.slice(1);
                console.log("Cliente creado")  
                swal({
                    title: "Cliente Creado con éxito",                
                    icon: "success",
                    button: "OK",
                  })
                  .then(res => {
                    window.location ='/manager.html'
                  });
                this.firstName=""
                this.lastName=""
                this.email=""            
                this.password=""
            })
            .catch(error =>{               
                this.errorC = true;                     
                this.msg_addClient= error.response.data                  
                      
            })            
        },   
        loadDataClient(){
            const urlParam = new URLSearchParams(window.location.search);
            const id = urlParam.get('id');        
            
            axios.get(`/api/admin/clients/${id}`)
                  .then(res => {                
                    this.client= res.data   
                    this.accounts = res.data.accounts.sort((a,b) => parseInt(a.id - b.id))
                    this.cards = res.data.cards.sort((a,b) => parseInt(a.id - b.id)) 
                    this.loans = res.data.loans.sort((a,b) => parseInt(a.id - b.id))                     
                    
                })
                  .catch(err => err.message)
            },
        viewClient(e){
            console.log(e.target)
            let id = e.target.firstChild.value 
            console.log(id)
            window.location.href = `./client.html?id=${id}`
        },
        deleteAll(){  
            this.selectedClients.forEach((client)=>{
                axios.delete(client)
              .then(res => this.loadClient())
              })
            this.selectedClients.length = 0
        },
        modify(cliente){
                axios.patch(cliente,{
                firstName: this.newFirstName,
                lastName: this.newLastName,
                email: this.newEmail
                })
              .then(res => this.loadClient())
            this.newFirstName =""
            this.newLastName =""
            this.newEmail =""
            //this.selectedClients.length = 0
        },
        createLoan(){
            let paymnts = this.payments.toString()
            console.log(paymnts)

            // name:"",
            // maxAmount:"",
            // payments:[],
            // interest:"",
            // name,
            // @RequestParam String maxAmount,
            // @RequestParam String payments,
            // @RequestParam String interest

            axios.post('/api/admin/newloans',`name=${this.name}&maxAmount=${this.maxAmount}&payments=${paymnts}&interest=${this.interest}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})            
            .then(res=>{                
                console.log("Cliente creado")  
                swal({
                    title: "Préstamo creado con éxito",                
                    icon: "success",
                    button: "OK",
                  })
                  .then(res => {
                    window.location.reload();
                  });
                this.name=""
                this.maxAmount=""
                this.payments=""            
                this.interest=""
            })
            .catch(error =>{               
                this.errorL = true;                     
                this.msg_newLoan= error.response.data     
                console.log(error)
            })    
        },
        deleteAccClient(e){
            let accNum = e.target.firstChild.value            
            console.log(accNum)
          axios.delete(`/api/admin/accounts/delete/${accNum}`,{headers:{'content-type':'application/x-www-form-urlencoded'}}) 
          .then(res => {
              console.log("cuenta eliminada")    
              swal({
                title: "¿Estas seguro que quieres borrar la cuenta?",
                text: "Una vez eliminada no podras recuperarla",
                icon: "warning",
                buttons: true,
                dangerMode: true,
              })
              .then((willDelete) => {
                if (willDelete) {
                  swal("La cuenta fue eliminada", {
                    icon: "success",
                  })
                  .then(res => {
                    window.location.reload();         
                  });                  
                } else {
                  swal("La cuenta está a salvo");
                }
              });                        
          })
          .catch(error=>{
              console.log("No se pudo eliminar la cuenta")                
              console.log(error)      

              this.errorA = true
              this.msg_delAccount = error.response.data 
          })
        },
        mostrarBotonModify(){
            this.modifyC= !this.modifyC
            },
        mostrarBotonDelete(){
            this.deleteC=!this.deleteC
            },
        mostrarInput(){
            this.radioButton=true
            },
        notSure(array){
            return  this.selectedClients.length = 0
        },
        searchClient(e){
                    e.preventDefault()
                    this.clientes.forEach(client =>{
                    if(!this.search.includes(this.firstName)){
                        this.searchClients = true
                    }
                    })
                    }

        },
    computed:{
        showSelectedClients(){
            let uno = [...this.selectedClients]
            return this.clientes.filter(client => uno.includes(client._links.self.href))
            },
        filterClient(){            
            return this.clientes.filter(client => client.firstName.toLowerCase().match(this.search.toLowerCase()) || client.lastName.toLowerCase().match(this.search.toLowerCase()))
            },
    }

})

app.mount("#app")