import axios from "axios";
import {BASE_URL} from "../common/Settings";

class MLPredictorService {
    getAccuracy = async (algorithm) => {
        const {data: response} = await axios.get(BASE_URL + '/ml-accuracy?algorithm='+algorithm);
        return response;
    }
}

export default new MLPredictorService();