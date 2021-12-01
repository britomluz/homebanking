const AppChat = {
    data() {
      return {
        counter: 0,
        coll:[],
        toggle: false,
        time:"",
        inputText:"",
        userText:[],
        botResponse:[],
        botResponseDos:[],
        btnConsults: false,          
        btnProblem: false,
        btnCons: false,
        btnPromo: false,
        mail: false,        
        envioYRec: false,      
        cards: false,        
        loans: false,   
      }
    },
    created(){
        this.firstBotMessage()                          
    },
    methods:{       
        collapsibleF(){      
            setTimeout(() => {
                this.toggle = !this.toggle;            
            }, 200)                  
        },
        showBtn(){
            this.btn = true
            },
        cleanChat(){
            this.btnCuentas = false
            this.btnCuentas= false          
            this.btnProblem= false
            this.btnCons= false
            this.btnPromo= false
            this.mail= false
            this.envioYRec= false
            this.cards= false
            this.loans= false
        },        
        optionsConsults(){
            setTimeout(() => {
                this.btnCons = !this.btnCons;
            }, 350)            
        },
        optionsConsss(){
            setTimeout(() => {
                this.promo = !this.promo;
            }, 350)            
        },
        cardss(){            
                    
            setTimeout(() => {
                this.cards = !this.cards;    
            }, 350)
        },        
        loanss(){
            setTimeout(() => {
                this.loans = !this.loans;
            }, 500)            
        },
        optionsProblem(){
            setTimeout(() => {
                this.btnProblem = !this.btnProblem;
            }, 350)            
        },               
        optionsPromo(){
            setTimeout(() => {
                this.btnPromo = !this.btnPromo;
            }, 350)
        },
        enviosYRec(){            
                    
            setTimeout(() => {
                this.envioYRec = !this.envioYRec;    
            }, 350)
        },        
        mails(){
            setTimeout((btn) => {
                this.mail = !this.mail;
            }, 500)            
        },
        getTime() {
            let today = new Date();
            hours = today.getHours();
            minutes = today.getMinutes();
        
            if (hours < 10) {
                hours = "0" + hours;
            }
        
            if (minutes < 10) {
                minutes = "0" + minutes;
            }
        
            let time = hours + ":" + minutes;
            return time;
        },
        firstBotMessage() {   
            this.time = this.getTime();
          //  console.log(this.time)               
        },
    },
  }
  
  Vue.createApp(AppChat).mount('#appChat')
  