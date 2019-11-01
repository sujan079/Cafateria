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
    order_by:{
        type:String,
        ref:'user',
        require:true
    },
    order_date:{
        type:String,
        require:true
    },
    order_time:{
        type:String,
        require:true
    },
    date:{
        type:Date,
        default:Date.now()
    }
});

module.exports=mongoose.model('order-history',OrderHistorySchema);
