import axios from "axios";
import {BASE_URL} from "../common/Settings";

class MLPredictorService {
    executeClassifier = async (algorithm, file) => {
        const formData = new FormData();
        formData.append("file", file);
        formData.append("algorithm", algorithm);
        const {data: response} = await axios.post(BASE_URL + '/classify/classify-ml', formData,
            {
                headers: {
                    "Content-Type": "multipart/form-data"
                },
            });
        return response;
    }
}

// eslint-disable-next-line import/no-anonymous-default-export
export default new MLPredictorService();