const express=require("express");
const morgan=require("morgan");
const body_parser=require("body-parser");

const app=express();

app.use(body_parser.urlencoded({extended:true}))
app.use(body_parser.json())

app.use(morgan("dev"));


app.use((req,res,next)=>{
    return res.status(200).json({
        message:"Working"
    })
})

module.exports=app;