const express=require("express");
const morgan=require("morgan");
const mongoose=require('mongoose');
const body_parser=require("body-parser");

const app=express();

const menuItemRoutes=require('./api/routes/menu_items.routes');
const counterRoutes=require('./api/routes/counter.routes');

app.use(body_parser.urlencoded({extended:true}))
app.use(body_parser.json())

app.use(morgan("dev"));

mongoose.connect(`mongodb+srv://${process.env.MONGODB_ATLAS_USER}:${process.env.MONGODB_ATLAS_PW}@cafateriacluster-lvoze.mongodb.net/cafateria?retryWrites=true&w=majority`,
{useUnifiedTopology:true,useNewUrlParser:true})
.then(()=>{
    console.log("Database Connect Success")
})
.catch(err=>{
    console.log(err)
})

app.use('/menu-items',menuItemRoutes);
app.use('/counter',counterRoutes);

app.use((req,res,next)=>{
    return res.status(404).json({
        message:"NOT FOUND"
    })
})

module.exports=app;