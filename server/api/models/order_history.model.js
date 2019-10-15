const mongoose=require('mongoose');

const OrderHistorySchema=mongoose.Schema({
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
    },
    quantity:{
        type:Number,
        default:1
    },
    orderBy:{
        type:mongoose.Schema.Types.ObjectId,
        ref:'user',
        require:true
    },
    orderDate:{
        type:String,
        require:true
    },
    orderTime:{
        type:String,
        require:true
    }
});

module.exports=mongoose.model('order-history',OrderHistorySchema);
