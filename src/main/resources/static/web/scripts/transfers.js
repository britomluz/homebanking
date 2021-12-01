
const app = Vue.createApp({
    data(){
        return{
            clients:[],
            accounts:[],
            account:[],
            transactions:[],
            amount:"",
            description:"",
            originNumberAccount:"",
            destinyNumberAccount:"",
            error_newTransfer:"",
            errorTransfer:false,
            

            accountByDefault:"",
            accountDestinyByDefault:"",
            accountsTransfer:"",

            prevBtns: "",
            nextBtns: "",
            progress: "",
            formSteps: "",
            progressSteps: [],             
            progressActive:'',

            
             ownAccounts:"",
             othersAccounts:"",
             showSelectAccounts:"",

             //schedule
             schedule:"",
             favContact:[],
             checkboxSchedule:"",
             accountContact:"",
             contacts:[],
             favs:[],
             nameContact:"",
             accountContact:"",
             typeContact:"",
             idAccount:"",
             errorContact:false,
             error_msgContact:"",
            

            
        };
    },
    created(){
        this.idAccount = this.accountParam()
        this.transferParam()
        this.loadClient()  
        this.loadData()         
    },
    methods:{
        loadClient(){
            axios.get("/api/clients/current")
            .then(res => {
              this.clients = res.data
              this.accounts = res.data.accounts.sort((a,b) => parseInt(a.id - b.id))
              this.cards = res.data.cards.sort((a,b) => parseInt(a.id - b.id)) 
              this.contacts = res.data.contacts.sort((a,b) => parseInt(a.id - b.id))

              this.accountByDefault = this.accounts.filter(acc => acc.id == this.idAccount)
              this.accountsTransfer = this.accounts.filter(acc => acc.id != this.idAccount).sort((a,b) => parseInt(a.id - b.id))
             
              this.accountDestinyByDefault = this.contacts.filter(acc => acc.accountContact == this.acc)

              this.favs = this.contacts.filter(card => card.type == 'FAV').sort((a,b) => parseInt(a.id - b.id))

              this.originNumberAccount = this.accountByDefault[0].number

              
              this.filterCards()                 
          })
            .catch(err => err.message)  
        },  
        loadData(){
            const urlParam = new URLSearchParams(window.location.search);
            const id = urlParam.get('id');
            console.log()
            axios.get(`/api/clients/current/accounts/${id}`)
                  .then(res => {                
                    this.account = res.data          
                    this.transactions = res.data.transactions
                })
                  .catch(err => err.message)
            },    
        accountParam(){
            const Param = new URLSearchParams(window.location.search);
            const idAcc = Param.get('id');
            
            return idAcc
        },
        transferParam(){
            const Param = new URLSearchParams(window.location.search);
            const trAcc = Param.get('account');
            if(trAcc != null){                
                this.showSelectAccounts = "othersAccount"
                this.destinyNumberAccount = trAcc
            } else {
                this.destinyNumberAccount = ""
            }
            
            return trAcc
        },
        createTransactions(){
            axios.post('/api/transactions',`amount=${this.amount}&description=${this.description}&originNumberAccount=${this.originNumberAccount}&destinyNumberAccount=${this.destinyNumberAccount}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})            
                .then(res =>{
                    console.log("Transferencia realizada con éxito!")
                    console.log(res)                     
                    swal({
                        title: "¡Bien hecho!",
                        text: `La transferencia se ha realizado con éxito \n \n Cuenta de origen: ${this.originNumberAccount} \n Cuenta de destino: ${this.destinyNumberAccount} \n Importe: $${this.amount} \n Descripción: ${this.description}\n \n \n ¿Quieres descargar el comprobante?  `,
                        icon: "success",                        
                        buttons: true,
                        dangerMode: true,
                      })
                      .then((willDelete) => {
                        if (willDelete) {
                            console.log("comprobante desc")
                            this.downloadPdfTransaction()
                          swal({
                              title: "¡Bien hecho!",
                              text: "Comprobante descargado",
                              icon: "success",
                              button: "ok",                               
                          })
                          .then(res =>{ 
                            
                            window.location ='/web/accounts.html'
                          })
                        } else {                                                    
                            window.location ='/web/accounts.html'
                        }
                      });                                           
                })        
                .catch(error =>{
                    console.log("Algo salio mal")     
                    
                    this.errorTransfer = true;     
                    this.error_newTransfer = error.response.data                             
                    console.log(error.response.data)
                })
        },
        addContact(){     
            this.checkboxSchedule = this.$refs.checkboxSchedule                           
            axios.post("/api/clients/current/contacts",`nameContact=${this.nameContact}&accountContact=${this.accountContact}&type=${this.typeContact}`, {headers:{'content-type':'application/x-www-form-urlencoded'}}) 
            .then(res => {
                console.log("Contacto agendado")           
                swal({
                    title: "¡Bien hecho!",
                    text: "Contacto agregado con éxito",
                    icon: "success",
                    button: "OK",                    
                  })
                  .then(res => {
                    window.location.reload();                   
                  });                                  
                                   
            })
            .catch(error=>{
                console.log("No se pudo generar la tarjeta")
                
                this.errorContact = true; 
                this.error_msgContact= error.response.data 

                console.log(error.response.data)         
                // console.log(error.response.data)     
            })
        },  
        editContact(e){
            console.log(e.target.firstChild.value)
            this.acc = e.target.firstChild.value
            console.log(this.acc)
            axios.patch("/api/clients/contacts/edit",`accountContact=${this.acc}`, {headers:{'content-type':'application/x-www-form-urlencoded'}}) 
            .then(res => {
                console.log("Contacto cambiado")      

                window.location.reload(); 
                                   
            })
            .catch(error=>{
                console.log("No se pudo editar el contacto")
                   
            })

        },
        logout(){
            axios.post('/api/logout')
            .then( res => {
                window.location ='/web/index.html'
            })
            .catch(err =>{
                console.log(err)
            })
        },              
        updateForms(btn, form1, form2, step){
            this.nextStep(btn, form1, form2);
            this.nextProgressBar(btn, step);
            
         },
        backForms(btn, form1, form2, step){
            this.prevStep(btn, form1, form2);
            this.prevProgressBar(btn, step);          
        },
        nextStep(btn, form1, form2){        
            this.nextBtns = btn; //next1       
        
            this.formSteps1 = form1; //form1
            this.formSteps2 = form2; //form2   

            this.formSteps1.classList.remove("form-step-active");
            this.formSteps2.classList.add("form-step-active");
       },
        prevStep(btn, form1, form2){        
            this.prevBtns = btn; //next1        
            
            this.formSteps1 = form1; //form2
            this.formSteps2 = form2; //form1 

            this.formSteps1.classList.add("form-step-active");
            this.formSteps2.classList.remove("form-step-active");   
            this.errorTransfer = false     
       },
        nextProgressBar(btn, step){        
            this.progressSteps1 = step       
            this.progressSteps1.classList.add("progress-step-active");               


            this.progress = this.$refs.progress                
            this.progressActive='progress-step-active'

            if(this.progressSteps1 == this.$refs.progressStep2){
                this.progress.style.width = "50%"; 
            } else if (this.progressSteps1 == this.$refs.progressStep3){
                this.progress.style.width = "100%";
            }
      },
        prevProgressBar(btn, step){        
            this.progressSteps1 = step       
            this.progressSteps1.classList.remove("progress-step-active");        

            this.progressActive='progress-step-active'
            
            if(this.progressSteps1 == this.$refs.progressStep2){
                this.progress.style.width = "0%"; 
            } else if (this.progressSteps1 == this.$refs.progressStep3){
                this.progress.style.width = "50%";
            }            
      },    
      downloadPdfTransaction(){            
        axios.post('/api/pdf/transaction',`amount=${this.amount}&description=${this.description}&originNumberAccount=${this.originNumberAccount}&destinyNumberAccount=${this.destinyNumberAccount}`,{ responseType: 'blob' }, {headers:{"Content-type": "application/pdf" }})                    
           .then((res) => {
             let disposition = res.headers['content-disposition']
             let filename = decodeURI(disposition.substring(21))
             let blob = new Blob([res.data], {type: 'application/pdf'});
             let objectUrl = URL.createObjectURL(blob);
             let link = document.createElement("a");
            // let filename = "transaccion"+ this.idTransfer;  
             link.href = objectUrl;
             link.setAttribute("download", filename);//link.download = filename
             document.body.appendChild(link);
             link.click();
           })
           .catch(err=> {
             console.log("No se puede descargar el pdf");
             });
     },        
      
    },
    computed:{
        btnDisabled(){            
            if(this.amount !== "" && this.description !== "" && this.originNumberAccount !== "" && this.destinyNumberAccount !== ""){
                return false
            } else {
                return true
            }
        },
        myAccounts(){
            return this.accounts.filter(acc => acc.number != this.originNumberAccount)
        }
    },
})
app.mount("#app");