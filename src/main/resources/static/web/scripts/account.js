const app = Vue.createApp({
    data(){
        return{
            clients:[],            
            accounts:[],
            account:[],
            transactions:[],
            filterDescription:'',
            filterType:[],
            filterRange:50000,
            filterDay:'',
            filterMonth:'',
            filterYear:'',
            objExpenses:[],

            //
            errorAccs: false,
            msg_createAccs:"",
            errorPdfDate: false,
            error_newPdfDate:"",

            //pagination
            elementsPerPage:10,
            transfersPerPage:[],
            actualPage:1,
            pageUno:"",
            acPage:1,
            arrayFiltrado:[],
            ini:"",
            end:"",

            idTransfer:"",


            //filter date to date
            dateFrom:"",
            dateTo:"",
        };
    },
    
    created(){        
        this.loadData()         
        this.loadClient()   
                   
    },
    mounted(){
        
    },
    methods:{
        loadClient(){
            axios.get("/api/clients/current")
            .then(res => {
              this.clients = res.data
              this.accounts = res.data.accounts                
              
              this.getDataPage(1)  
             
          })
            .catch(err => err.message)  
        }, 
        loadData(){
        const urlParam = new URLSearchParams(window.location.search);
        const id = urlParam.get('id');        
        
        axios.get(`/api/clients/current/accounts/${id}`)
              .then(res => {                
                this.account = res.data          
                this.transactions = res.data.transactions
                this.orderTransactions()              
                this.prueba()
                this.suma()
                
            })
              .catch(err => err.message)
        },
        showAccounts(e){
            let id = e.target.id
            window.location.href = `./account.html?id=${id}`
        },
        showTransfers(e){
            let id = e.target.id
            window.location.href = `./transfer.html?id=${id}`
        },
        orderTransactions(){
            this.transactions = this.transactions.sort((a,b) =>{
                if(a.id > b.id){
                    return -1
                } else if(a.id < b.id){
                    return 1
                } else {
                    return 0
                }
            });
            return this.transactions
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
        totalPages(){
            return Math.ceil(this.arrayFiltrado.length / this.elementsPerPage)
        },
        getDataPage(numPage){
            
           // console.log(this.acPage)
            //console.log(this.actualPage)

            this.actualPage = numPage
            this.transfersPerPage = [];

            this.ini = (numPage * this.elementsPerPage) - this.elementsPerPage
            this.end = (numPage * this.elementsPerPage)

            this.transfersPerPage = this.arrayFiltrado.slice(this.ini,this.end)  
            //this.transfersPerPage =  this.transfersPerPage.slice(ini,end)
            //this.transfersPerPage =  this.arrayFiltrado.slice(ini,end)

        },
        getPrevPage(){            
            if(this.actualPage > 1){
                this.actualPage --
            }
            this.getDataPage(this.actualPage)
        },
        getNextPage(){            
            if(this.actualPage < this.totalPages()){
                this.actualPage ++
            }
            this.getDataPage(this.actualPage)
        },
        isActive(numPage){
            return numPage == this.actualPage ? "active" : ""
           
        },
        downloadPdfTransfer(e){            
           this.idTransfer = e.target.firstChild.value 
           axios.post("/api/pdf/transfer",`id=${this.idTransfer}`,{ responseType: 'blob' }, {headers:{"Content-type": "application/pdf" }})
              .then((res) => {
                let disposition = res.headers['content-disposition']
                let filename = decodeURI(disposition.substring(21))
                let blob = new Blob([res.data], {type: 'application/pdf'});
                let objectUrl = URL.createObjectURL(blob);
                let link = document.createElement("a");
                //let filename = "transaccion"+ this.idTransfer;  

                link.href = objectUrl;
                link.setAttribute("download", filename);
                document.body.appendChild(link);
                link.click();
              })
              .catch(err=> {
                console.log("No se puede descargar el pdf");
                });
        },
        downloadPdfListOfTransfer(e){                    

            let id = e.target.id
                        
            axios.post("/api/pdf/listoftransfer",`from=${this.dateFrom}&to=${this.dateTo}&accountId=${id}`,{ responseType: 'blob' }, {headers:{"Content-type": "application/pdf" }})
               .then((res) => {
                 let disposition = res.headers['content-disposition']
                 let filename = decodeURI(disposition.substring(21))
                 let blob = new Blob([res.data], {type: 'application/pdf'});
                 let objectUrl = URL.createObjectURL(blob);
                 let link = document.createElement("a");
                 //let filename = "transaccion"+ this.idTransfer;  
 
                 link.href = objectUrl;
                 link.setAttribute("download", filename);
                 document.body.appendChild(link);
                 link.click();
               })
               .catch(err=> {
                 console.log("No se puede descargar el pdf");    

                 console.log(err.response.status) 

                 if(err.response.status === 403){
                    this.errorPdfDate=true
                    this.error_newPdfDate= "La fecha ingresada no puede ser mayor a la fecha actual"
                 } else if(err.response.status === 411){
                    this.errorPdfDate=true
                    this.error_newPdfDate= "No se encontró ninguna transferencia en ese rango de fechas"
                 }else if(err.response.status === 409){
                    this.errorPdfDate=true
                    this.error_newPdfDate= "Los campos no pueden estar vacíos"
                 }

                 console.log(err.response.status) 
                 });
         },
    },
    computed:{
        filterByDescription(){
                    this.arrayFiltrado =   this.transactions.filter(transaction => transaction.description.toLowerCase().match(this.filterDescription.toLowerCase()))
                                                            .filter(transaction => this.filterType.includes(transaction.type) || this.filterType.length === 0)
                                                            .filter(transaction => transaction.amount <= this.filterRange)
                                                            .filter(transaction => transaction.date.slice(0,2).match(this.filterDay))
                                                            .filter(transaction => transaction.date.slice(3,5).match(this.filterMonth))
                                                            .filter(transaction => transaction.date.slice(6,10).match(this.filterYear)) 

                                                            
                
                    this.transfersPerPage = this.arrayFiltrado.slice(this.ini,this.end)                                     
                    
                    return this.arrayFiltrado
        },
        
        prueba(){
           // console.log(this.transactions[0].date.slice(0,2))
            //console.log(this.transactions[0].date.slice(3,5))
            //console.log(this.transactions[0].date.slice(6,10))
            // console.log("--------")            

            // this.objExpenses = this.transactions.map( month => {
            //     let expenses ={
            //         month: month.date.slice(3,5),
            //         year: month.date.slice(6,10),  
            //         expense: month.amount,
            //         type: month.type                    
            //     }
            //     return expenses
            // });

            // type = 'CREDIT'
            // month = 10
            // year = 2021

            // console.log(this.objExpenses)            
            // console.log("----------------------")

            // let totalExpenses = 0 
            // for(i = 0; i < this.objExpenses.length; i ++){   
            //     if(this.objExpenses[i].type == type && this.objExpenses[i].month == month && this.objExpenses[i].year == year) {
            //         totalExpenses+= this.objExpenses[i].expense
            //     }                
            // }
            // console.log(totalExpenses)
        },       
        
    }
})
app.mount("#app");