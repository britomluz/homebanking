const app = Vue.createApp({
    data(){
        return{
            clients:[],
            accounts:[],
            cards:[],   
            card:[],
            creditCards:[],
            debitCards:[],
            cardId:"",
            

            //card expired
            dateNow:"",
            dateExpired:"",
            cardExpired:"",
            proxExpired: false,
            expired: false,

            //card resume
            totalPay:"",
            numberAccount:"",
            errorR: false,
            error_newPayResume:"",
            pays:[],


            monthNow:"",
            yearNow:"",
            monthExp:"",
            yearExp:"",


        };
    },
    created(){
        this.loadClient()  
        this.loadData()
    },
    methods:{
        loadClient(){
            axios.get("/api/clients/current")
            .then(res => {
              this.clients = res.data
              this.accounts = res.data.accounts
              this.cards = res.data.cards.sort((a,b) => parseInt(a.id - b.id))  
              this.loadData()  
              this.filterCards()    
          })
            .catch(err => err.message)  
        },          
        cardFlip(e){                   
           e.target.closest('.card').classList.toggle('flipCard')                       
           
        },
        filterCards(){
          this.creditCards = this.cards.filter(card => card.type == 'CREDITO')
          this.debitCards = this.cards.filter(card => card.type == 'DEBITO')
           
        }, 
        expiredCard(card){
            let dateNow = new Date()
            //let dateNow = new Date("2027-09-26T12:07")     
             
             //convert string to int and then int to Date
            let a = Date.parse(card.thruDate.slice(0,2)+"/01/"+card.thruDate.slice(3,5))
                        
            //convert int to Date
            let dateExpired = new Date(a)                        
            dateExpired.setMonth(dateExpired.getMonth()-3)
            //console.log(this.dateExpired.toISOString().substring(0,10)) 
            
            //var dateString = new Date().toISOString().split("T")[0];
            //new Date().toISOString().substring(0,10)
            let cardExpired = new Date(a) 

            if(dateNow > dateExpired && dateNow < cardExpired){
                this.proxExpired = true
            } else if(dateNow >= cardExpired){
                this.expired = true
            }            
            
            // console.log(dateNow)
            // console.log(dateExpired)
            // console.log(cardExpired)

                  
            // console.log(dateNow > dateExpired && dateNow < cardExpired)       
            // console.log(dateNow >= cardExpired)      
           
        },      
        showCard(e){
            let id = e.target.id
            window.location.href = `./card-resume.html?id=${id}`
             console.log(id)
        },
        loadData(){
            const urlParam = new URLSearchParams(window.location.search);
            const id = urlParam.get('id');
            
            axios.get(`/api/clients/current/cards/${id}`)
                  .then(res => {                
                    this.card = res.data
                    this.pays = res.data.pays.sort((a,b) => parseInt(b.id - a.id))      
                    this.totalPays()                        
                    
                })
                  .catch(err => err.message)
            },
        totalPays(){            
            let sum = 0;
            let pays = this.card.pays
            
            pays.forEach(item => {                 
                sum += item.amountPayments;
            })
            
            return this.totalPay = sum
        },
        payResume(card){
            
            let cardId = card.id
           
            axios.post("/api/clients/current/card/pays",`totalPay=${this.totalPay}&numberAccount=${this.numberAccount}&id=${cardId}`, {headers:{'content-type':'application/x-www-form-urlencoded'}}) 
            .then(res => {
                
                swal({
                  title: "¡Bien hecho!",
                  text: "Pago realizado con éxito",
                  icon: "success",
                  button: "OK",
                })
                .then(res => {
                    window.location ='/web/accounts.html'
                });
                
            })
            .catch(error=>{                
                this.errorR= true
                this.error_newPayResume= error.response.data
                console.log(error.response.data)     
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
    },
})
app.mount("#app");