import axios from "axios";
import {BASE_URL} from "../common/Settings";

class ViewPredictionService {
    getAllPredictions = async () => {
        const {data: response} = await axios.get(BASE_URL + '/view-prediction/find-all');
        return response;
    }
}

export default new ViewPredictionService();