const mongoose=require('mongoose');


const orderSchema=mongoose.Schema({
    _id:mongoose.Schema.Types.ObjectId,
    user:{
        type:String,
        require:true,
        ref:"user"
    },
    menu_items:{
        type:[
                {
                    itemName:{type:String,require:true},
                    price:{type:Number,require:true},
                    categories:{type:[String],require:true},
                    quantity:{type:Number,default:1}
                }
            ],
    }

})

module.exports=mongoose.model('order',orderSchema);