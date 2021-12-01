
const app = Vue.createApp({
    data(){
        return{
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
            months: ["Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre",],
            days:[],
            todayBackground: "",
            prevArrow:"",
            nextArrow:"",
        };
    },
    created(){
        this.renderCalendar()
    },
    methods:{
        renderCalendar(){                    
              
        this.date.setDate(1)
        
        this.month = this.date.getMonth();
        this.monthTitle = this.months[this.date.getMonth()];      
       
        let options = { weekday: 'long', month: 'long', day: 'numeric' };
        this.dateTitle = this.date.toLocaleDateString("es-ES", options);
        
        
        this.lastDay = new Date(this.date.getFullYear(), this.date.getMonth() + 1, 0).getDate() // 0 last day of month, 1 first day of month
        this.prevLastDay = new Date(this.date.getFullYear(), this.date.getMonth(), 0).getDate()
        

        //index first and last day
        this.firstDayIndex = this.date.getDay();
        this.lastDayIndex = new Date(this.date.getFullYear(), this.date.getMonth() + 1, 0).getDay();
        
        //array with lasts days of previuos month
        for (let x = this.firstDayIndex; x > 0; x--) {            
            this.prevDays.push(this.prevLastDay - x + 1)         
       }
       
        for (let i = 1; i <= this.lastDay; i++) {                        
            this.days.push(i)                    
       }

    
       console.log(this.todayBackground)      
      

       this.nextDay = 7- this.lastDayIndex -1
       
       //array with first days of next month
       for (let j = 1; j <= this.nextDay; j++) {            
        this.nextDays.push(j)         
    }
        },
    nextMonth(e){
        console.log(e.target)
        this.nextArrow = this.$refs.
        this.date.setMonth(this.date.setMonth() + 1)
        this.renderCalendar();
    }
        
    },
})
app.mount("#app");
