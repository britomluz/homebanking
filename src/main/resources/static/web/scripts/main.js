const app = Vue.createApp({
    data(){
        return{
            //general data
            clients:[],
            accounts:[], 
            loans:[],
            cards:[],
            movements:[],
            newEmail:"",
            btnEdit: false,

            //coins
            coins:[],
            calcDolar:"",                        
            quantityDolar: "",            
            dolar:[],    
            errorM:false,            

            //cards
            errorCards:false,            
            error_newAccount:"", 
            error_newCard:"",            
            color:"",
            type:"",
            numberAccount:"",
            numAcc: false,
            //delete
            cardId:"",

            

            // profile image
            imagePreviewDos: "",
            imageUploaderDos: "",
            CLOUDINARY_URL:"",
            CLOUDINARY_UPLOAD_PRESET:"",
            urlImg:"",

            //multistep form
            prevBtns: "",
            nextBtns: "",
            progress: "",
            formSteps: "",
            progressSteps: "",              
            progressActive:'',        
            
            //calendar
            date: new Date(),
            dateTitle:"",
            lastDay:"",
            prevLastDay:"",
            firstDayIndex:"",
            lastDayIndex:"",
            prevDays:[],
            nextDays:[],
            nextDay:"",
            month:"",
            monthTitle:"",   
            weekdays:["Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"],
            months: ["Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre",],
            days:[],
            todayBackground: "",
            prevArrow:"",
            nextArrow:"",
            dayNow:"",
            monthNow:"",
            yearNow:"",            
            weekdayNow:"",

            //
            errorAccs: false,
            msg_createAccs:"",

            //contact
            schedule:"",
             favContact:[],
             checkboxSchedule:"",
             accountContact:"",
             contacts:[],
             favs:[],
             nameContact:"",
             accountContact:"",
             typeContact:"",
             accFav:"",
             accountDestinyByDefault:"",
             accountsTransfer:"",

             //contact form
             nameForm: "",
             mailForm: "",
             subjectForm: "",
             bodyForm:"",

             //create account
             newAccType:""

        };
    },
    created(){
        this.loadClient()
        this.showCoins()       
        this.renderCalendar()        
         
    },
    methods:{
        loadClient(){
            axios.get("/api/clients/current")
            .then(res => {
              this.clients = res.data
              this.accounts = res.data.accounts.sort((a,b) => parseInt(a.id - b.id))
              this.loans = res.data.loans.sort((a,b) => parseInt(a.id - b.id))
              this.cards = res.data.cards.sort((a,b) => parseInt(a.id - b.id))
              this.contacts = res.data.contacts.sort((a,b) => parseInt(a.id - b.id))
              //this.contacts= this.contacts.sort((a,b) => parseInt(a.id - b.id))
              this.movements = res.data.movements.sort((a,b) => parseInt(b.id - a.id))

              //console.log(this.movements)

              this.favs = this.contacts.filter(contact => contact.type == 'FAV')
              this.imgClient = this.clients.url
            
          })
            .catch(err => err.message)  
        },  
        showCoins(){
            axios.get("https://www.dolarsi.com/api/api.php?type=valoresprincipales")
              .then(res => {
                this.coins = res.data 
                this.dolar = res.data[0].casa                
                            
            })
              .catch(err => err.message)
        },              
        editClient(){
            axios.patch("/api/clients/current",`email=${this.newEmail}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
          .then(res =>{
              console.log(res)
              swal({
                title: "¡Has cambiado tu correo con éxito!",
                text: "Por favor, ingresa nuevamente a tu cuenta",
                icon: "success",
                button: "OK",
              })
              .then(res => {
                window.location ='/web/index.html'
              });
          })        
          .catch(error =>{
            console.log(error.response.data)
        })
            this.newEmail =""  
         },                      
         showAccount(e){            
             let id = e.target.id
             window.location.href = `./account.html?id=${id}`
             console.log(id)
         }, 
         showTransfers(e){
            let id = e.target.id
            window.location.href = `./transfer.html?id=${id}`
        },
        showBtnEdit(){
            this.btnEdit = !this.btnEdit
         },        
         calculateDolar(){
            this.quantityDolar =  "U$S " + (parseInt(this.calcDolar) / parseInt(this.dolar.venta)).toFixed(2)
            console.log(this.quantityDolar)
         },
         createAccount(e){
             e.preventDefault()
            axios.post("/api/clients/current/accounts",`type=${this.newAccType}`, {headers:{'content-type':'application/x-www-form-urlencoded'}}) 
            .then(res => {
                console.log("cuenta creada")
                //location.reload();
                swal({
                  title: "¡Bien hecho!",
                  text: "Tu nueva cuenta fue creada con éxito",
                  icon: "success",
                  button: "OK",
                })
                .then(res => {
                  window.location.reload();
                });
                
            })
            .catch(error=>{
                this.errorM = true;                     
                this.error_newAccount = error.response.data   
                //console.log(error.response)         
                console.log(error.response.data)     
            })
         },
        boolCardDebit(e){                      

          if(e.target.value == 'DEBITO'){
            this.numAcc = true
        } else if(e.target.value == 'CREDITO'){
          this.numAcc = false
        }
        },
        createCard(){           

            axios.post("/api/clients/current/cards",`color=${this.color}&type=${this.type}&numberAccount=${this.numberAccount}`, {headers:{'content-type':'application/x-www-form-urlencoded'}}) 
            .then(res => {
                console.log("tarjeta creada")                
                swal({
                    title: "¡Bien hecho!",
                    text: "La tarjeta fue creada con éxito",
                    icon: "success",
                    button: "OK",
                  })
                  .then(res => {
                    window.location ='/web/cards.html'
                  });
            })
            .catch(error=>{
                console.log("No se pudo generar la tarjeta")
                this.errorCards = true;                     
                this.error_newCard = error.response.data   
                console.log(this.error_newCard)         
                
            })
        },      
        deleteCard(){          
          console.log(this.cardId)
          axios.delete(`/api/clients/current/cards/delete/${this.cardId}`,{headers:{'content-type':'application/x-www-form-urlencoded'}}) 
          .then(res => {
              console.log("tarjeta eliminada")                
              swal({
                title: "Tu solicitud de baja fue aprobada",                
                icon: "success",
                button: "OK",
              })
              .then(res => {
                window.location ='/web/home.html'
              });
          })
          .catch(error=>{
              console.log("No se pudo eliminar la tarjeta")                
              console.log(error)      
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
        //peticion para modificar la url de la imagen de perfil en la base de datos
         editPhoto(){
            axios.patch("/api/clients/current/photo",`url=${this.urlImg}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
          .then(res =>{
            console.log("Imagen cambiada con éxito!")
              console.log(res)     
              window.location.reload();       
          })        
          .catch(error =>{
            console.log(error.response.data)
        })
         },
         //metodo para cambiar la imagen de perfil desde el front
         uploadImageCloud(event){
            this.imagePreviewDos = this.$refs.imagePreviewDos
            this.imageUploaderDos = this.$refs.imageUploaderDos
            this.CLOUDINARY_URL = 'https://api.cloudinary.com/v1_1/luz-brito/image/upload'
            this.CLOUDINARY_UPLOAD_PRESET = 'qda9s173'
            
            console.log(event.target.files[0])
            const fileImg = event.target.files[0]

            const formData = new FormData;

            formData.append('file', fileImg)
            formData.append('upload_preset', this.CLOUDINARY_UPLOAD_PRESET)
            
            axios.post(this.CLOUDINARY_URL, formData, {headers: {'Content-Type': 'multipart/form-data'}})
            .then (res => {
               console.log("Imagen cargada!")
               console.log(res)
               this.imagePreviewDos.src = res.data.secure_url

               this.urlImg =  this.imagePreviewDos.src                  
            
            })

         },      
         //metodo para cambiar los pasos para el formulario
         updateForms(btn, form1, form2, step){
           this.nextStep(btn, form1, form2);
           this.nextProgressBar(btn, step);
         },
         
         backForms(btn, form1, form2, step){
          this.prevStep(btn, form1, form2);
          this.prevProgressBar(btn, step);          
        },
        //boton siguiente del form
       nextStep(btn, form1, form2){        
        this.nextBtns = btn; //next1
       
        
        this.formSteps1 = form1; //form1
        this.formSteps2 = form2; //form2       


        this.formSteps1.classList.remove("form-step-active");
        this.formSteps2.classList.add("form-step-active");
       },
       //boton volver del form
       prevStep(btn, form1, form2){        
        this.prevBtns = btn; //next1        
        
        this.formSteps1 = form1; //form2
        this.formSteps2 = form2; //form1 

        this.formSteps1.classList.add("form-step-active");
        this.formSteps2.classList.remove("form-step-active"); 
                
        this.errorCards = false
        
       },
      nextProgressBar(btn, step){
        this.progressSteps1 = step       
        this.progressSteps1.classList.add("progress-step-active");               


        this.progress = this.$refs.progress        
        
        this.progressActive='progress-step-active'
        
        //si se esta en el form2 aumentar la progressbar al 50%, si esta en el form3 aumentarla al 100%
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
        
        //si se esta en el form2 volver la progressbar al 50%, si esta en el form1 volver al 0%
        if(this.progressSteps1 == this.$refs.progressStep2){
            this.progress.style.width = "0%"; 
        } else if (this.progressSteps1 == this.$refs.progressStep3){
            this.progress.style.width = "50%";
        }    
        
      },
      renderCalendar(){    
        this.yearNow = new Date().getFullYear()  

        this.date.setDate(1)        
        this.month = this.date.getMonth();       
        this.monthTitle = this.months[this.date.getMonth()];      
       
        let options = { weekday: 'long', month: 'long', day: 'numeric' };
        this.dateTitle = this.date.toLocaleDateString("es-ES", options);

        let dt = new Date();        
        this.dayNow = dt.getDate()
        this.weekdayNow = this.weekdays[dt.getUTCDay()]
        this.monthNow = this.months[dt.getUTCMonth()]
                       
        
        //qantity days in month
        this.lastDay = new Date(this.date.getFullYear(), this.date.getMonth() + 1, 0).getDate() // 0 last day of month, 1 first day of month
        this.prevLastDay = new Date(this.date.getFullYear(), this.date.getMonth(), 0).getDate()
        //console.log(this.prevLastDay)

        //index first and last day
        this.firstDayIndex = this.date.getDay();
        this.lastDayIndex = new Date(this.date.getFullYear(), this.date.getMonth() + 1, 0).getDay();
        
        //array with lasts days of previuos month
        for (let x = this.firstDayIndex; x > 0; x--) {            
            this.prevDays.push(this.prevLastDay - x + 1)         
       }       
       
        //array with all days in month
        for (let i = 1; i <= this.lastDay; i++) {                        
            this.days.push(i) 
                           
       }

       this.nextDay = 7- this.lastDayIndex -1       
       //array with first days of next month
       for (let j = 1; j <= this.nextDay; j++) {            
        this.nextDays.push(j)         
      }
        },    
      nextMonth(){     
          this.nextArrow = this.$refs.nextArrow

          this.date.setMonth(this.date.getMonth()+1)

          this.prevDays = []
          this.days = []
          this.nextDays = []

          this.renderCalendar();
      },
      prevMonth(){     
          this.prevArrow = this.$refs.prevArrow
          
          this.date.setMonth(this.date.getMonth()-1)

          this.prevDays = []
          this.days = []
          this.nextDays = []

          this.renderCalendar();
     },
      transferContact(e){

        console.log(e.target.firstChild.value)
        this.accFav = e.target.firstChild.value
        //this.destinyNumberAccount = e.target.firstChild.value
        
        window.location.href = `./transfer.html?account=${this.accFav}`
    },
      downloadExtractAccounts(){
        window.location ='/api/pdf/accounts'
    },
    sendMailForm(){
      let spinner = this.$refs.spinnerContainer
      
      spinner.style.display='flex';

      axios.post('/api/sendMail', `name=${this.nameForm}&mail=${this.mailForm}&subject=${this.subjectForm}&body=${this.bodyForm}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
      .then(res => {
        console.log("Mail enviado")  
        spinner.style.display='none';         
        swal({
            title: "¡Bien hecho!",
            text: "Mail enviado con éxito. Te responderemos a la brevedad",
            icon: "success",
            button: "OK",                    
          })
          .then(res => {            
            this.nameForm="",                   
            this.mailForm="",
            this.subjectForm="",
            this.bodyForm=""
          });                                  
                           
      })
    }
  
}
})
app.mount("#app");