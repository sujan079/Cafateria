const mongoose=require('mongoose');

const menuItemSchema=mongoose.Schema({
    _id:mongoose.Schema.Types.ObjectId,
    itemName:{
        type:String,
        require:true
    },
    price:{
        type:Number,
        require:true
    },
    categories:{
        type:[String],
        require:true
    }
})



module.exports = mongoose.model("menu-item",menuItemSchema);