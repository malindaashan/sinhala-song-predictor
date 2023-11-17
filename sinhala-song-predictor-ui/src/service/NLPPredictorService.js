import axios from "axios";
import {BASE_URL} from "../common/Settings";

class NLPPredictorService {
    getPredictionNLP = async (text,embedding) => {
        let obj ={};
        obj.text =text;
        const {data: response} = await axios.post(BASE_URL + '/nlp/predict?embedding='+embedding, obj);
        return response;
    }
}

export default new NLPPredictorService();