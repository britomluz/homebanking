const app = Vue.createApp({
    data() {
        return {            
            name: "",            
            number: "",            
            cvv: "",
            adress: "",
            payments:"",
            total: "7542",
            description: "Compra en SHOP STORE",

            errorS: false,
            error_newShop:"",

            thruDate:"",
        };
    },
    created() {

    },
    methods: {
        newShop(){
            axios.post('/api/shop', {
                name: this.name,
                number: this.number,
                cvv: this.cvv,
                adress: this.adress,
                payments: this.payments,
                total: this.total,
                description: this.description
            },  { headers: { 'content-type': 'application/json' }})
                .then(res => {
                    console.log("pago realizado con exito")
                    swal({
                        title: "¡Bien hecho!",
                        text: "Tu compra fue realizada con éxito!",
                        icon: "success",
                        button: "OK",
                    })
                    .then(res => {
                        window.location.reload();
                    });

                })
                .catch(error => {
                    console.log("Algo salio mal")
                    this.errorS = true
                    this.error_newShop = error.response.data                    
                })
        },
    },
    
})
app.mount("#app");