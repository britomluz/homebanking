const app = Vue.createApp({
    data(){
        return{
            //loan
            clients:[],     
            accounts:[]  ,
            loans:[],
            payments:[],
            maxAmount:0,
            amountLoan:"",
            paymentsLoan:"",
            toAccountLoan:"",
            nameLoan:"",
            idLoan:"",
            errorLoan:false,
            error_newLoan:"",
            amountPayments:"",

            //multistepform
            prevBtns: "",
            nextBtns: "",
            progress: "",
            formSteps: "",
            progressSteps: [],             
            progressActive:'',
            
        };
    },
    
    created(){                    
        this.loadClient()           
        this.getDataLoan()
        
                   
    },    
    methods:{
        loadClient(){
            axios.get("/api/clients/current")
            .then(res => {
              this.clients = res.data
              this.accounts = res.data.accounts
             
          })
            .catch(err => err.message)  
        },       
            
        getDataLoan(){
            axios.get("/api/loans")
            .then(res => {
              this.loans = res.data.sort((a,b) => parseInt(a.id - b.id))                
          })
            .catch(err => err.message)  
        },
        createLoan(){
            axios.post('/api/loans', {
                id: this.idLoan,
                amount: this.amountLoan,
                payments: this.paymentsLoan,
                accountTo: this.toAccountLoan
            }, {headers: {'content-type' : 'application/json'}})
            .then( res => {
                console.log("credito creado con exito")
                swal({
                    title: "¡Bien hecho!",
                    text: "Tu credito fue aprobado con exito!",
                    icon: "success",
                    button: "OK",                    
                  })
                  .then(res => {
                    window.location ='/web/accounts.html'                  
                  }); 
                
            })
            .catch(err =>{
                console.log(err)
                this.errorLoan = true;     
                this.error_newLoan = err.response.data 
                console.log(err.response.data) 
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
            this.paymentsList()                 

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

            this.errorLoan = false
      }, 
        paymentsList(){
            let paym = this.loans.filter(pay => pay.name === this.nameLoan)

            this.payments = paym[0].payments
            this.maxAmount = paym[0].maxAmount
            this.idLoan = paym[0].id

            // console.log(this.payments)
            // console.log(this.idLoan)
            // console.log(this.maxAmount)
        },       
        
    },
    computed:{
        btnDisabledLoan(){            
            if(this.amountLoan !== "" && this.paymentsLoan !== "" && this.toAccountLoan !== "" && this.nameLoan !== ""){
                return false
            } else {
                return true
            }
        },             
    }
})
app.mount("#app");