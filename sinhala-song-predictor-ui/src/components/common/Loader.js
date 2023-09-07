import React from "react";
import {Modal} from "@mui/material";

const Loader = () => {
    const [showDownloader, setShowDownloader] = React.useState(true);
    return (
        <Modal
            open={showDownloader}
        >
            <div id="loader" className="center-image" style={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                height: '70vh'

            }}>
                <img src={process.env.PUBLIC_URL + '/loader.gif'} id="loader-img" alt="loader"/>
            </div>
        </Modal>
    )
}

export default Loader;
