const express=require("express");
const morgan=require("morgan");
const mongoose=require('mongoose');
const body_parser=require("body-parser");

const app=express();

const menuItemRoutes=require('./api/routes/menu_items.routes');
const todayRoutineRoutes=require('./api/routes/today_routine.routes');
const userRoutes=require('./api/routes/user.routes');
const orderHistoryRoutes=require('./api/routes/order_history.routes');
const orderRoutes=require('./api/routes/orders.routes');

app.use(body_parser.urlencoded({extended:true}))
app.use(body_parser.json())

app.use(morgan("dev"));

mongoose.connect(`mongodb+srv://sujan079:9JHGvzEVyjIcIbXW@cafateriacluster-lvoze.mongodb.net/cafateria?retryWrites=true&w=majority`,
{useUnifiedTopology:true,useNewUrlParser:true})
.then(()=>{
    console.log("Database Connect Success")
})
.catch(err=>{
    console.log(err)
})

app.use('/menu-items',menuItemRoutes);
app.use('/today-routine',todayRoutineRoutes);
app.use('/order-history',orderHistoryRoutes);
app.use('/users',userRoutes);
app.use('/orders',orderRoutes);

app.use((req,res,next)=>{
    return res.status(404).json({
        message:"NOT FOUND"
    })
})

module.exports=app;