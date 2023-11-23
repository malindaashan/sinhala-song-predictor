import axios from "axios";
import {BASE_URL} from "../common/Settings";

class HybridPredictorService {
    executeHybridClassifier = async (algorithm, file, text, embedding) => {
        let obj = {};
        obj.text = text;
        const formData = new FormData();
        formData.append("file", file);
        formData.append("algorithm", algorithm);
        formData.append("embedding", embedding);
        formData.append("text", text);
        const {data: response} = await axios.post(BASE_URL + '/hybrid/predict', formData,
            {
                headers: {
                    "Content-Type": "multipart/form-data"
                },
            });
        return response;
    }
}

// eslint-disable-next-line import/no-anonymous-default-export
export default new HybridPredictorService();