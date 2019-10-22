const mongoose=require('mongoose');


const orderSchema=mongoose.Schema({
    _id:mongoose.Schema.Types.ObjectId,
    user:{
        type:mongoose.Schema.Types.ObjectId,
        require:true,
        ref:"user"
    },
    menu_items:{
        type:[
                {
                    item_name:{type:String,require:true},
                    item_price:{type:Number,require:true},
                    category:{type:String,require:true},
                    quantity:{type:Number,default:1}
                }
            ],
    }

})

module.exports=mongoose.model('order',orderSchema);